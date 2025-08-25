package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

class VedtakTest {
    @Test
    fun `En boolsk opplysninger oppfylt dersom opplysningen finnes og har verdi true`() {
        Vedtak(
            behandlingId = UUID.randomUUID(),
            utfall = Vedtak.Utfall.INNVILGET,
            opplysninger =
                setOf(
                    DagpengerOpplysning.HarSamordnet(true),
                    DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(false),
                ),
        ).let {
            it.oppfylt<DagpengerOpplysning.HarSamordnet>() shouldBe true
            it.oppfylt<DagpengerOpplysning.OppfyllerKravTilMinsteinntekt>() shouldBe false

            it.oppfylt<DagpengerOpplysning.OppfyllerKravTilMobilitet>() shouldBe false
        }
    }

    @Test
    fun `En boolsk opplysning er ikke oppfylt dersom opplysningen finnes og har verdi false`() {
        Vedtak(
            behandlingId = UUID.randomUUID(),
            utfall = Vedtak.Utfall.INNVILGET,
            opplysninger =
                setOf(
                    DagpengerOpplysning.HarSamordnet(true),
                    DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(false),
                ),
        ).let {
            it.ikkeOppfylt<DagpengerOpplysning.HarSamordnet>() shouldBe false
            it.ikkeOppfylt<DagpengerOpplysning.OppfyllerKravTilMinsteinntekt>() shouldBe true

            it.ikkeOppfylt<DagpengerOpplysning.OppfyllerKravTilMobilitet>() shouldBe false
        }
    }
}
