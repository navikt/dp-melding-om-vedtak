package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.Aldersgrense
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallBarnSomGirRettTilBarnetillegg
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallGSomGisSomGrunnlagVedVerneplikt
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallPermitteringsuker
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallPermitteringsukerFisk
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallStønadsuker
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.AntallStønadsukerSomGisVedOrdinæreDagpenger
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.ArbeidsinntektSiste12Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.ArbeidsinntektSiste36Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.BarnetilleggIKroner
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.BruktBeregningsregelGrunnlag
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.Egenandel
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.ErInnvilgetMedVerneplikt
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FastsattNyArbeidstidPerUke
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FastsattVanligArbeidstidPerUke
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.ForeldrepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FørsteMånedAvOpptjeningsperiode
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FørsteMånedOgÅrForInntektsperiode1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FørsteMånedOgÅrForInntektsperiode2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.FørsteMånedOgÅrForInntektsperiode3
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.Grunnlag
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste12Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste36Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarBruktBeregningsregelArbeidstidSiste6Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.HarSamordnet
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.InntektskravSiste12Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.InntektskravSiste36Måneder
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.KravTilProsentvisTapAvArbeidstid
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.OmsorgspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.OpplæringspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.PleiepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.Prøvingsdato
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SeksGangerGrunnbeløp
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SisteMånedAvOpptjeningsperiode
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SisteMånedOgÅrForInntektsperiode1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SisteMånedOgÅrForInntektsperiode2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SisteMånedOgÅrForInntektsperiode3
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SvangerskapspengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.SykepengerDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.UføreDagsats
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.UtbetaltArbeidsinntektPeriode1
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.UtbetaltArbeidsinntektPeriode2
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.OpplysningTyper.UtbetaltArbeidsinntektPeriode3
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Companion.NULL_OPPLYSNING
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.BOOLSK
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.DATO
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.FLYTTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.HELTALL
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Datatype.TEKST
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.BARN
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.ENHETSLØS
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.KRONER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.TIMER
import no.nav.dagpenger.vedtaksmelding.model.vedtak.Opplysning.Enhet.UKER
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.AVSLÅTT
import no.nav.dagpenger.vedtaksmelding.model.dagpenger.Vedtak.Utfall.INNVILGET
import java.time.LocalDate
import java.time.Month
import java.util.UUID

class VedtakMapper(vedtakJson: String) {
    private val vedtak: JsonNode
    private val objectMapper: ObjectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    init {
        vedtak = objectMapper.readTree(vedtakJson)
    }

    fun vedtak(): Vedtak {
        return Vedtak(
            behandlingId = behandlingId,
            utfall = utfall,
            vilkår = vilkår,
            opplysninger = vedtakOpplysninger + inntjeningsperiodeOpplysninger,
            fagsakId = fagsakId,
        )
    }

    private val behandlingId =
        UUID.fromString(vedtak.get("behandlingId").asText())
            ?: throw IllegalArgumentException("behandlingId mangler")

    val fagsakId = vedtak.get("fagsakId")?.asText() ?: throw FagsakIdMangler("FagsakId mangler i path /fagsakId")

    private val utfall: Utfall =
        vedtak.get("fastsatt")?.get("utfall")?.asBoolean()?.let { utfall ->
            when (utfall) {
                true -> INNVILGET
                false -> AVSLÅTT
            }
        } ?: throw UtfallMangler("Utfall mangler i path: /fastsatt/utfall")

    private val vilkår: Set<Vilkår> =
        vedtak.get("vilkår")?.map { vilkårNode ->
            Vilkår(
                navn = vilkårNode["navn"].asText(),
                status =
                    vilkårNode["status"].asText().let {
                        when (it) {
                            "Oppfylt" -> Vilkår.Status.OPPFYLT
                            else -> Vilkår.Status.IKKE_OPPFYLT
                        }
                    },
            )
        }?.toSet() ?: throw VilkårMangler("Vilkår mangler i path: /vilkår")

    private val vedtakOpplysninger: Set<Opplysning> =
        setOf(
            vedtak.finnOpplysningAt(
                opplysningTekstId = Grunnlag.opplysningTekstId,
                jsonPointer = "/fastsatt/grunnlag/grunnlag",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningAt(
                opplysningTekstId = DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel.opplysningTekstId,
                jsonPointer = "/fastsatt/sats/dagsatsMedBarnetillegg",
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningAt(
                opplysningTekstId = Prøvingsdato.opplysningTekstId,
                jsonPointer = "/virkningsdato",
                datatype = DATO,
            ),
            vedtak.finnOpplysningAt(
                opplysningTekstId = FastsattNyArbeidstidPerUke.opplysningTekstId,
                jsonPointer = "/fastsatt/fastsattVanligArbeidstid/nyArbeidstidPerUke",
                datatype = FLYTTALL,
                enhet = TIMER,
            ),
            vedtak.finnOpplysningAt(
                opplysningTekstId = FastsattVanligArbeidstidPerUke.opplysningTekstId,
                jsonPointer = "/fastsatt/fastsattVanligArbeidstid/vanligArbeidstidPerUke",
                datatype = FLYTTALL,
                enhet = TIMER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = KravTilProsentvisTapAvArbeidstid,
                datatype = FLYTTALL,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = Aldersgrense,
                datatype = HELTALL,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = InntektskravSiste12Måneder,
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = InntektskravSiste36Måneder,
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = ArbeidsinntektSiste12Måneder,
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = ArbeidsinntektSiste36Måneder,
                datatype = HELTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = AntallGSomGisSomGrunnlagVedVerneplikt,
                datatype = FLYTTALL,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = BruktBeregningsregelGrunnlag,
                datatype = TEKST,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = HarBruktBeregningsregelArbeidstidSiste6Måneder,
                datatype = BOOLSK,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = HarBruktBeregningsregelArbeidstidSiste12Måneder,
                datatype = BOOLSK,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = HarBruktBeregningsregelArbeidstidSiste36Måneder,
                datatype = BOOLSK,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = UtbetaltArbeidsinntektPeriode1,
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = UtbetaltArbeidsinntektPeriode2,
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = UtbetaltArbeidsinntektPeriode3,
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = AntallStønadsukerSomGisVedOrdinæreDagpenger,
                datatype = HELTALL,
                enhet = UKER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget,
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = AntallBarnSomGirRettTilBarnetillegg,
                datatype = HELTALL,
                enhet = BARN,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = BarnetilleggIKroner,
                datatype = FLYTTALL,
                enhet = KRONER,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = FørsteMånedAvOpptjeningsperiode,
                datatype = DATO,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = SisteMånedAvOpptjeningsperiode,
                datatype = DATO,
            ),
            vedtak.finnOpplysningFraType(
                opplysningType = SeksGangerGrunnbeløp,
                datatype = HELTALL,
                enhet = KRONER,
            ),
        ) + vedtak.lagOpplysningerFraKvoter() + vedtak.lagOpplysningerForSamordning()

    private val inntjeningsperiodeOpplysninger = vedtakOpplysninger.finnInntjeningsPeriode()

    private fun Set<Opplysning>.finnInntjeningsPeriode(): Set<Opplysning> {
        val opptjeningsperiodeOpplysninger = mutableSetOf<Opplysning>()

        val opptjeningsperiodeStart: LocalDate? =
            this.singleOrNull {
                it.opplysningTekstId == FørsteMånedAvOpptjeningsperiode.opplysningTekstId
            }?.let { opplysning ->
                LocalDate.parse(opplysning.råVerdi())
            }
        val opptjeningsperiodeSlutt: LocalDate? =
            this.singleOrNull {
                it.opplysningTekstId == SisteMånedAvOpptjeningsperiode.opplysningTekstId
            }?.let { opplysning ->
                LocalDate.parse(opplysning.råVerdi())
            }

        if (opptjeningsperiodeStart != null && opptjeningsperiodeSlutt != null) {
            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = FørsteMånedOgÅrForInntektsperiode1.opplysningTekstId,
                    råVerdi = opptjeningsperiodeStart.plusYears(2).norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )
            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = FørsteMånedOgÅrForInntektsperiode2.opplysningTekstId,
                    råVerdi = opptjeningsperiodeStart.plusYears(1).norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )
            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = FørsteMånedOgÅrForInntektsperiode3.opplysningTekstId,
                    råVerdi = opptjeningsperiodeStart.norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )

            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = SisteMånedOgÅrForInntektsperiode1.opplysningTekstId,
                    råVerdi = opptjeningsperiodeSlutt.norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )
            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = SisteMånedOgÅrForInntektsperiode2.opplysningTekstId,
                    råVerdi = opptjeningsperiodeSlutt.minusYears(1).norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )
            opptjeningsperiodeOpplysninger.add(
                Opplysning(
                    opplysningTekstId = SisteMånedOgÅrForInntektsperiode3.opplysningTekstId,
                    råVerdi = opptjeningsperiodeSlutt.minusYears(2).norskMånedOgÅr(),
                    datatype = TEKST,
                    enhet = ENHETSLØS,
                ),
            )
        }
        return opptjeningsperiodeOpplysninger
    }

    private fun LocalDate.norskMånedOgÅr() =
        when (this.month) {
            Month.JANUARY -> "januar ${this.year}"
            Month.FEBRUARY -> "februar ${this.year}"
            Month.MARCH -> "mars ${this.year}"
            Month.APRIL -> "april ${this.year}"
            Month.MAY -> "mai ${this.year}"
            Month.JUNE -> "juni ${this.year}"
            Month.JULY -> "juli ${this.year}"
            Month.AUGUST -> "august ${this.year}"
            Month.SEPTEMBER -> "september ${this.year}"
            Month.OCTOBER -> "oktober ${this.year}"
            Month.NOVEMBER -> "november ${this.year}"
            Month.DECEMBER -> "desember ${this.year}"
        }

    private fun JsonNode.finnOpplysningFraType(
        opplysningType: OpplysningTyper,
        datatype: Datatype,
        enhet: Enhet = ENHETSLØS,
    ): Opplysning {
        return finnOpplysningAt(
            opplysningTekstId = opplysningType.opplysningTekstId,
            jsonPointer = "/opplysninger",
            datatype = datatype,
            enhet = enhet,
        ) { opplysning ->
            opplysning.find { it["opplysningTypeId"].asText() == opplysningType.opplysningTypeId.toString() }?.findValue("verdi")?.asText()
        }
    }

    private fun JsonNode.lagOpplysningerForSamordning(): Set<Opplysning> {
        val opplysninger = mutableSetOf<Opplysning>()
        val samordnedeYtelser = this.at("/fastsatt/samordning")

        if (samordnedeYtelser is MissingNode) return emptySet()
        val samordnetOpplysning = harSamordnet(samordnedeYtelser)
        opplysninger.add(samordnetOpplysning)
        samordnedeYtelser.forEach { samordnetYtelse ->
            when (samordnetYtelse["type"].asText()) {
                "Sykepenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = SykepengerDagsats.opplysningTekstId,
                            råVerdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Pleiepenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = PleiepengerDagsats.opplysningTekstId,
                            råVerdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Omsorgspenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = OmsorgspengerDagsats.opplysningTekstId,
                            råVerdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Opplæringspenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = OpplæringspengerDagsats.opplysningTekstId,
                            råVerdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Uføre dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = UføreDagsats.opplysningTekstId,
                            råVerdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Foreldrepenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = ForeldrepengerDagsats.opplysningTekstId,
                            råVerdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )

                "Svangerskapspenger dagsats" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = SvangerskapspengerDagsats.opplysningTekstId,
                            råVerdi = samordnetYtelse["beløp"].asText(),
                            datatype = FLYTTALL,
                            enhet = KRONER,
                        ),
                    )
            }
        }
        return opplysninger
    }

    private fun harSamordnet(samordnedeYtelser: JsonNode): Opplysning {
        return when (samordnedeYtelser.size()) {
            0 ->
                Opplysning(
                    opplysningTekstId = HarSamordnet.opplysningTekstId,
                    råVerdi = false.toString(),
                    datatype = BOOLSK,
                )

            else ->
                Opplysning(
                    opplysningTekstId = HarSamordnet.opplysningTekstId,
                    råVerdi = true.toString(),
                    datatype = BOOLSK,
                )
        }
    }

    private fun JsonNode.lagOpplysningerFraKvoter(): Set<Opplysning> {
        val opplysninger = mutableSetOf<Opplysning>()
        val kvoter = this.at("/fastsatt/kvoter")

        if (kvoter is MissingNode) return emptySet()

        kvoter.forEach { kvote ->
            when (kvote["navn"].asText()) {
                "Dagpengeperiode" -> bestemStønadsukerOpplysning(opplysninger, kvote)
                "Verneplikt" -> {
                    bestemStønadsukerOpplysning(opplysninger, kvote)
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = ErInnvilgetMedVerneplikt.opplysningTekstId,
                            råVerdi = true.toString(),
                            datatype = BOOLSK,
                        ),
                    )
                }
                "Permitteringsperiode" -> {
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = AntallPermitteringsuker.opplysningTekstId,
                            råVerdi = kvote["verdi"].asText(),
                            datatype = HELTALL,
                            enhet = UKER,
                        ),
                    )
                }
                "FiskePermitteringsperiode" -> {
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = AntallPermitteringsukerFisk.opplysningTekstId,
                            råVerdi = kvote["verdi"].asText(),
                            datatype = HELTALL,
                            enhet = UKER,
                        ),
                    )
                }
                "Egenandel" ->
                    opplysninger.add(
                        Opplysning(
                            opplysningTekstId = Egenandel.opplysningTekstId,
                            råVerdi = kvote["verdi"].asText(),
                            datatype = HELTALL,
                            enhet = KRONER,
                        ),
                    )
            }
        }

        return opplysninger
    }

    private fun bestemStønadsukerOpplysning(
        opplysninger: MutableSet<Opplysning>,
        kvote: JsonNode,
    ) {
        val stønadsukerOpplysning =
            Opplysning(
                opplysningTekstId = AntallStønadsuker.opplysningTekstId,
                råVerdi = kvote["verdi"].asText(),
                datatype = HELTALL,
                enhet = UKER,
            )

        when (kvote["navn"].asText()) {
            // Ikke optimalt, men dersom Verneplikt og Dagpengeperiode finnes i kvoter, så tar Verneplikt presedens.
            // Dette burde endres i vedtaksAPIet siden vi tolker regelverk her, og det er ikke vår oppgave.
            "Dagpengeperiode" -> {
                if (opplysninger.none { it.opplysningTekstId == AntallStønadsuker.opplysningTekstId }) {
                    opplysninger.add(stønadsukerOpplysning)
                }
            }

            "Verneplikt" -> {
                opplysninger.removeIf { it.opplysningTekstId == AntallStønadsuker.opplysningTekstId }
                opplysninger.add(stønadsukerOpplysning)
            }
        }
    }

    private fun JsonNode.finnOpplysningAt(
        opplysningTekstId: String,
        jsonPointer: String,
        datatype: Datatype,
        enhet: Enhet = ENHETSLØS,
        predicate: (JsonNode) -> String? = { node -> node.asText() },
    ): Opplysning {
        return this.at(jsonPointer).let {
            when (it) {
                is MissingNode -> NULL_OPPLYSNING
                else -> {
                    when (val verdi = predicate(it)) {
                        null -> NULL_OPPLYSNING
                        else ->
                            Opplysning(
                                opplysningTekstId = opplysningTekstId,
                                datatype = datatype,
                                enhet = enhet,
                                råVerdi = verdi,
                            )
                    }
                }
            }
        }
    }
}

class VilkårMangler(message: String) : RuntimeException(message)

class UtfallMangler(message: String) : RuntimeException(message)

class FagsakIdMangler(message: String) : RuntimeException(message)
