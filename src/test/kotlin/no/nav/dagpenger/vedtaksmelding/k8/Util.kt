package no.nav.dagpenger.vedtaksmelding.k8

import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.models.V1Secret
import io.kubernetes.client.util.ClientBuilder
import io.kubernetes.client.util.KubeConfig
import java.io.FileReader
import java.time.OffsetDateTime

// Hente azuread eller tokenx secret for  app
// jwker.nais.io -> tokenx,  azurerator.nais.io -> azuread
fun getAuthEnv(
    app: String,
    type: String = "jwker.nais.io",
): Map<String, String> {
    // file path to your KubeConfig
    val kubeConfigPath = System.getenv("KUBECONFIG")

    // IF this fails do kubectl get pod to aquire credentials
    val client: ApiClient = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(FileReader(kubeConfigPath))).build()
    Configuration.setDefaultApiClient(client)
    return CoreV1Api().listNamespacedSecret(
        "teamdagpenger",
    )
        .labelSelector("app=$app,type=$type")
        .execute()
        .items.also { secrets ->
            secrets.sortByDescending<V1Secret?, OffsetDateTime> { it?.metadata?.creationTimestamp }
        }.first<V1Secret?>()?.data!!.mapValues { e -> String(e.value) }
}

fun setAzureAuthEnv(
    app: String,
    type: String = "azurerator.nais.io",
    test: () -> Unit,
) {
    getAuthEnv(app, type).let { env ->
        try {
            env.forEach { (key, value) -> System.setProperty(key, value) }
            test()
        } finally {
            env.keys.forEach { key -> System.clearProperty(key) }
        }
    }
}
