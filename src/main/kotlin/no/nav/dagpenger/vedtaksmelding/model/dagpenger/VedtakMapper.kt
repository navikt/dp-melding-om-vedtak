package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class VedtakMapper(
    vedtakJson: String,
) {
    fun vedtak(): Vedtak {
        return Vedtak(
            behandlingId = behandlingResultatData.behandlingId(),
            utfall = utfall(),
            opplysninger = this.build(),
        )
    }

    private val behandlingResultatData = BehandlingResultatData(vedtakJson)

    private fun utfall(): Vedtak.Utfall {
        return when {
            behandlingResultatData.harRett() -> {
                Vedtak.Utfall.INNVILGET
            }

            else -> {
                Vedtak.Utfall.AVSLÅTT
            }
        }
    }

    private fun MutableSet<DagpengerOpplysning<*, *>>.addIfPresent(func: () -> DagpengerOpplysning<*, *>) {
        try {
            this.add(func())
        } catch (e: BehandlingResultatData.BehandlingResultatOpplysningIkkeFunnet) {
            logger.debug { "Opplysning ikke funnet: ${e.message} for behandlingId: ${behandlingResultatData.behandlingId()}" }
        }
    }

    private fun build(): Set<DagpengerOpplysning<*, *>> {
        val opplysningerFraData =
            buildSet {
                this.addIfPresent { DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.InntektskravSiste12Måneder(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.InntektskravSiste36Måneder(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.ArbeidsinntektSiste12Måneder(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.ArbeidsinntektSiste36Måneder(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.BruktBeregningsregelGrunnlag(behandlingResultatData) }
                this.addIfPresent {
                    DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(
                        behandlingResultatData,
                    )
                }
                this.addIfPresent {
                    DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(
                        behandlingResultatData,
                    )
                }
                this.addIfPresent {
                    DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(
                        behandlingResultatData,
                    )
                }
                this.addIfPresent { DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3(behandlingResultatData) }
                this.addIfPresent {
                    DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(
                        behandlingResultatData,
                    )
                }
                this.addIfPresent {
                    DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                        behandlingResultatData,
                    )
                }
                this.addIfPresent { DagpengerOpplysning.BarnetilleggIKroner(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.SisteMånedAvOpptjeningsperiode(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.SeksGangerGrunnbeløp(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.Aldersgrense(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.Grunnlag(behandlingResultatData) }
                this.addIfPresent {
                    DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(
                        behandlingResultatData,
                    )
                }
                this.addIfPresent { DagpengerOpplysning.Prøvingsdato(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.FastsattVanligArbeidstidPerUke(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.FastsattNyArbeidstidPerUke(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.HarSamordnet(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.SykepengerDagsats(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.PleiepengerDagsats(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OmsorgspengerDagsats(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OpplæringspengerDagsats(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.UføreDagsats(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.ForeldrepengerDagsats(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.SvangerskapspengerDagsats(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.PeriodeSomGisVedVerneplikt(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.Egenandel(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilArbeidssøker(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravTilMobilitet(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravTilArbeidsfør(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravTilArbeidssøker(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravetTilOpphold(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.IkkeFulleYtelser(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilTaptArbeidstid(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilTapAvArbeidsinntekt(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.IkkeStreikEllerLockout(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilAlder(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilUtdanning(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerMedlemskap(behandlingResultatData) }
                this.addIfPresent {
                    DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget(
                        behandlingResultatData,
                    )
                }
                this.addIfPresent { DagpengerOpplysning.ErInnvilgetMedVerneplikt(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.AntallPermitteringsuker(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.AntallPermitteringsukerFisk(behandlingResultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravetTilPermittering(behandlingResultatData) }
                this.addIfPresent {
                    DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(behandlingResultatData)
                }
                DagpengerOpplysning.AntallStønadsuker.fra(this)?.let { this.add(it) }
            }

        val deriverteOpplysninger =
            opplysningerFraData.filterIsInstance<DeriverbarOpplysning>().flatMap {
                it.deriverteOpplysninger
            }

        val opplysnings = opplysningerFraData + deriverteOpplysninger

        return opplysnings
    }
}
