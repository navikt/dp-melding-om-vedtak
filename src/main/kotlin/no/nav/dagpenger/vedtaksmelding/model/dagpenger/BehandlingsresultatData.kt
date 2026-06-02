package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.dagpenger.vedtaksmelding.model.OpplysningDataException
import no.nav.dagpenger.vedtaksmelding.serder.defaultObjectMapper
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectMapper
import java.time.LocalDate
import java.util.UUID

private val logger = KotlinLogging.logger {}

class BehandlingsresultatData(
    json: String,
) {
    companion object {
        private val objectMapper: ObjectMapper = defaultObjectMapper()
    }

    private data class Rettighetsperiode(
        val fraOgMed: LocalDate,
        val tilOgMed: LocalDate? = null,
        val harRett: Boolean,
        val opprinnelse: String,
    )

    private val jsonNode = objectMapper.readTree(json)

    val opplysningNoder = jsonNode["opplysninger"]

    fun automatiskBehandling(): Boolean = jsonNode["automatisk"].asBoolean()

    fun behandletHendelseType() = jsonNode["behandletHendelse"]["type"].stringValue().uppercase()

    private val rettighetsperioder: List<Rettighetsperiode> = rettighetsperioder().sortedBy { it.fraOgMed }

    fun virkningsdato(): LocalDate {
        val nyeRettighetsperioder = rettighetsperioder.filter { it.opprinnelse == "Ny" }
        return when (utfall()) {
            Vedtak.Utfall.AVSLÅTT -> {
                // Ved ny rettighet, hentes første periode med opprinnelse "Ny".
                // Ved gjenopptak, hentes siste periode uten rett, siden opplysningene vil ha opprinnelse "Arvet".
                nyeRettighetsperioder
                    .firstOrNull { !it.harRett }
                    ?.fraOgMed
                    ?: rettighetsperioder
                        .lastOrNull()
                        ?.fraOgMed
                    ?: throw ManglendeVirkningsdato("Fant ingen rettighetsperiode med harRett = false for avslag om dagpenger")
            }

            Vedtak.Utfall.INNVILGET -> {
                nyeRettighetsperioder
                    .firstOrNull { it.harRett }
                    ?.fraOgMed
                    ?: throw ManglendeVirkningsdato("Fant ingen ny rettighetsperiode med harRett = true for innvilgelse av dagpenger")
            }

            Vedtak.Utfall.GJENOPPTAK -> {
                nyeRettighetsperioder
                    .firstOrNull { it.harRett }
                    ?.fraOgMed
                    ?: throw ManglendeVirkningsdato("Fant ingen ny rettighetsperiode med harRett = true for gjenopptak av dagpenger")
            }

            Vedtak.Utfall.STANS -> {
                nyeRettighetsperioder
                    .firstOrNull { !it.harRett }
                    ?.fraOgMed
                    ?: throw ManglendeVirkningsdato("Fant ingen ny rettighetsperiode med harRett = false for stans av dagpenger")
            }
        }
    }

    fun sisteDagMedRett(): LocalDate? {
        val nyeRettighetsperioder = rettighetsperioder.filter { it.opprinnelse == "Ny" }
        return when (utfall()) {
            Vedtak.Utfall.INNVILGET -> {
                nyeRettighetsperioder
                    .firstOrNull { it.harRett }
                    ?.tilOgMed
            }
            Vedtak.Utfall.GJENOPPTAK -> {
                nyeRettighetsperioder
                    .firstOrNull { it.harRett }
                    ?.tilOgMed
            }
            else -> null
        }
    }

    private fun rettighetsperioder(): List<Rettighetsperiode> {
        val nodes = jsonNode["rettighetsperioder"]
        return try {
            objectMapper.convertValue(
                nodes,
                object : TypeReference<List<Rettighetsperiode>>() {},
            )
        } catch (e: Exception) {
            logger.error(e) { "Fant ikke rettighetsperioder" }
            throw OpplysningDataException("Fant ikke rettighetsperioder")
        }
    }

    fun flyttall(id: UUID): Double = pickPeriodeForVirkningsdato(perioderFlyttall(id), id).verdi

    fun heltall(id: UUID): Int = pickPeriodeForVirkningsdato(perioderHeltall(id), id).verdi

    fun penger(id: UUID): Number = pickPeriodeForVirkningsdato(perioderPenger(id), id).verdi

    fun tekst(id: UUID): String = pickPeriodeForVirkningsdato(perioderTekst(id), id).verdi

    fun boolsk(id: UUID): Boolean = pickPeriodeForVirkningsdato(perioderBoolsk(id), id).verdi

    fun dato(id: UUID): LocalDate = pickPeriodeForVirkningsdato(perioderDato(id), id).verdi

    // TODO: Vurder om *Last-variantene faktisk trengs, eller om virkningsdato-basert oppslag
    // er riktig også for EgenandelGjenstående og AntallStønadsdagerSomGjenstår. Usikker på
    // opprinnelig intensjon — disse plukker siste periode i JSON-rekkefølge uten hensyn til virkningsdato.
    fun heltallLast(id: UUID): Int = lastPeriode(perioderHeltall(id), id).verdi

    fun pengerLast(id: UUID): Number = lastPeriode(perioderPenger(id), id).verdi

    private fun <V : Any> pickPeriodeForVirkningsdato(
        perioder: List<Periode<V>>,
        opplysningTypeId: UUID,
    ): Periode<V> {
        val matches = perioder.filter { it.inkludererDato(virkningsdato()) }
        if (matches.isEmpty()) {
            throw PeriodeIkkeFunnet(opplysningTypeId, virkningsdato())
        }
        if (matches.size > 1) {
            throw OpplysningDataException(
                "Fant flere enn èn periode for opplysning med opplysningTypeId $opplysningTypeId og virkningsdato ${virkningsdato()}",
            )
        }
        return matches.single()
    }

    private fun <V : Any> lastPeriode(
        perioder: List<Periode<V>>,
        opplysningTypeId: UUID,
    ): Periode<V> {
        if (perioder.isEmpty()) {
            throw PeriodeIkkeFunnet(opplysningTypeId, virkningsdato())
        }
        return perioder.last()
    }

    private fun JsonNode.isDato(): Boolean = toDateOrNull() != null

    private fun JsonNode.asDato(): LocalDate = toDateOrNull() ?: throw IllegalArgumentException("Kan ikke konvertere $this til LocalDate")

    private fun JsonNode.toDateOrNull(): LocalDate? =
        try {
            LocalDate.parse(asText())
        } catch (e: Exception) {
            logger.error(e) { "Kan ikke konvertere $this til LocalDate" }
            null
        }

    fun perioderHeltall(id: UUID): List<Periode<Int>> =
        perioder(id, "heltall") { node ->
            require(node.isInt) { "Forventet at heltall har int verdi, men var $node" }
            node.asInt()
        }

    fun perioderFlyttall(id: UUID): List<Periode<Double>> =
        perioder(id, "desimaltall") { node ->
            require(node.isNumber) { "Forventet at desimaltall har number verdi, men var $node" }
            node.asDouble()
        }

    fun perioderPenger(id: UUID): List<Periode<Number>> =
        perioder(id, "penger") { node ->
            require(node.isNumber) { "Forventet at penger har number verdi, men var $node" }
            if (node.isInt) node.asInt() else node.asDouble()
        }

    fun perioderTekst(id: UUID): List<Periode<String>> =
        perioder(id, "tekst") { node ->
            require(node.isTextual) { "Forventet tekstlig verdi, men var $node" }
            node.asString()
        }

    fun perioderBoolsk(id: UUID): List<Periode<Boolean>> =
        perioder(id, "boolsk") { node ->
            require(node.isBoolean) { "Forventet at boolsk har boolsk verdi, men var $node" }
            node.asBoolean()
        }

    fun perioderDato(id: UUID): List<Periode<LocalDate>> =
        perioder(id, "dato") { node ->
            require(node.isDato()) { "Forventet at dato har riktig dato verdi, men var $node" }
            node.asDato()
        }

    private fun <V : Any> perioder(
        opplysningTypeId: UUID,
        forventetDatatype: String,
        extract: (JsonNode) -> V,
    ): List<Periode<V>> {
        val opplysningNode =
            opplysningNoder
                .values()
                .filter { it["opplysningTypeId"].asString() == opplysningTypeId.toString() }
                .also {
                    if (it.isEmpty()) throw BehandlingResultatOpplysningIkkeFunnet(opplysningTypeId)
                    if (it.size > 1) {
                        throw OpplysningDataException(
                            "Fant flere enn èn opplysningstype med opplysningTypeId $opplysningTypeId",
                        )
                    }
                }.single()

        val perioderNode = opplysningNode["perioder"] ?: return emptyList()
        return perioderNode.values().map { periodeNode ->
            val verdiNode = periodeNode["verdi"]
            require(verdiNode["datatype"].asString() == forventetDatatype) {
                "Forventet datatype $forventetDatatype for opplysning $opplysningTypeId, men var ${verdiNode["datatype"]}"
            }
            val fraOgMedNode = periodeNode.get("gyldigFraOgMed")
            val tilOgMedNode = periodeNode.get("gyldigTilOgMed")
            Periode(
                verdi = extract(verdiNode["verdi"]),
                opprinnelse = Opprinnelse.fra(periodeNode["opprinnelse"].asString()),
                gyldigFraOgMed = if (fraOgMedNode == null || fraOgMedNode.isNull) null else fraOgMedNode.asDato(),
                gyldigTilOgMed = if (tilOgMedNode == null || tilOgMedNode.isNull) null else tilOgMedNode.asDato(),
            )
        }
    }

    // TODO: Kan kollapses til perioderPenger(opplysningTypeId).any { it.opprinnelse == NY }
    // (eller tilsvarende for andre datatyper). Vurder å fjerne denne i en egen runde.
    internal fun periodeMedOpprinnelseNyFinnes(opplysningTypeId: UUID): Boolean? =
        opplysningNoder
            .values()
            .singleOrNull {
                it["opplysningTypeId"].asString() == opplysningTypeId.toString()
            }?.let { opplysningNode ->
                opplysningNode["perioder"]
                    .values()
                    .any {
                        it["opprinnelse"].asString() == "Ny"
                    }
            }

    fun behandlingId(): UUID = jsonNode["behandlingId"].let { UUID.fromString(it.asString()) }

    fun utfall(): Vedtak.Utfall {
        val førteTil = jsonNode["førteTil"].asString()
        return when (førteTil) {
            "Innvilgelse" -> Vedtak.Utfall.INNVILGET
            "Avslag" -> Vedtak.Utfall.AVSLÅTT
            "Gjenopptak" -> Vedtak.Utfall.GJENOPPTAK
            "Stans" -> Vedtak.Utfall.STANS
            else -> {
                throw UtfallIkkeStøttet(førteTil)
            }
        }
    }

    data class BehandlingResultatOpplysningIkkeFunnet(
        val opplysningTypeId: UUID,
    ) : OpplysningDataException("Fant ikke opplysning med opplysningTypeId $opplysningTypeId")

    data class PeriodeIkkeFunnet(
        val opplysningTypeId: UUID,
        val virkningsdato: LocalDate,
    ) : OpplysningDataException("Fant ikke periode for opplysningTypeId $opplysningTypeId og virkningsdato $virkningsdato")

    data class UtfallIkkeStøttet(
        val førteTil: String,
    ) : OpplysningDataException("Behandlingsresultat som førte til '$førteTil' har ikke brevstøtte")

    class ManglendeVirkningsdato(
        message: String,
    ) : OpplysningDataException(message)
}
