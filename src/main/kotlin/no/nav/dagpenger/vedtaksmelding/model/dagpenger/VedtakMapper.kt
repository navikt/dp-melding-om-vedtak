package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class VedtakMapper(
    vedtakJson: String,
) {
    fun vedtak(): Vedtak =
        Vedtak(
            behandlingId = behandlingsresultatData.behandlingId(),
            utfall = utfall(),
            opplysninger = this.build(),
        )

    private val behandlingsresultatData = BehandlingsresultatData(vedtakJson)

    private fun utfall(): Vedtak.Utfall = behandlingsresultatData.utfall()

    private fun MutableSet<DagpengerOpplysning<*, *>>.addIfPresent(func: () -> DagpengerOpplysning<*, *>) {
        try {
            this.add(func())
        } catch (e: BehandlingsresultatData.BehandlingResultatOpplysningIkkeFunnet) {
            logger.debug { "Opplysning ikke funnet: ${e.message} for behandlingId: ${behandlingsresultatData.behandlingId()}" }
        }
    }

    private fun build(): Set<DagpengerOpplysning<*, *>> {
        val opplysningerFraData =
            buildSet {
                this.addIfPresent { DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.InntektskravSiste12Måneder(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.InntektskravSiste36Måneder(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.ArbeidsinntektSiste12Måneder(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.ArbeidsinntektSiste36Måneder(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.BruktBeregningsregelGrunnlag(behandlingsresultatData) }
                this.addIfPresent {
                    DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(
                        behandlingsresultatData,
                    )
                }
                this.addIfPresent {
                    DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(
                        behandlingsresultatData,
                    )
                }
                this.addIfPresent {
                    DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(
                        behandlingsresultatData,
                    )
                }
                this.addIfPresent { DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3(behandlingsresultatData) }
                this.addIfPresent {
                    DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(
                        behandlingsresultatData,
                    )
                }
                this.addIfPresent {
                    DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                        behandlingsresultatData,
                    )
                }
                this.addIfPresent { DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.BarnetilleggIKroner(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.SisteMånedAvOpptjeningsperiode(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.SeksGangerGrunnbeløp(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.Aldersgrense(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.Grunnlag(behandlingsresultatData) }
                this.addIfPresent {
                    DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(
                        behandlingsresultatData,
                    )
                }
                this.addIfPresent { DagpengerOpplysning.Virkningsdato(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.FastsattVanligArbeidstidPerUke(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.FastsattNyArbeidstidPerUke(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.HarSamordnet(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.SykepengerDagsats(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.PleiepengerDagsats(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OmsorgspengerDagsats(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OpplæringspengerDagsats(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.UføreDagsats(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.ForeldrepengerDagsats(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.SvangerskapspengerDagsats(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.PeriodeSomGisVedVerneplikt(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.Egenandel(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilArbeidssøker(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravTilMobilitet(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravTilArbeidsfør(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravTilArbeidssøker(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravetTilOpphold(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.IkkeFulleYtelser(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilTaptArbeidstid(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilTapAvArbeidsinntekt(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.IkkeStreikEllerLockout(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilAlder(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.KravTilUtdanning(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerMedlemskap(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.GodkjentLokalArbeidssøker(behandlingsresultatData) }
                this.addIfPresent {
                    DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget(
                        behandlingsresultatData,
                    )
                }
                this.addIfPresent { DagpengerOpplysning.ErInnvilgetMedVerneplikt(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.AntallPermitteringsuker(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.AntallPermitteringsukerFisk(behandlingsresultatData) }
                this.addIfPresent { DagpengerOpplysning.OppfyllerKravetTilPermittering(behandlingsresultatData) }
                this.addIfPresent {
                    DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(behandlingsresultatData)
                }
                DagpengerOpplysning.AntallStønadsuker.fra(this)?.let { this.add(it) }
                DagpengerOpplysning.SisteDagMedRett.fra(behandlingsresultatData)?.let { this.add(it) }
            }

        val deriverteOpplysninger =
            opplysningerFraData.filterIsInstance<DeriverbarOpplysning>().flatMap {
                it.deriverteOpplysninger
            }

        val opplysnings = opplysningerFraData + deriverteOpplysninger

        return opplysnings
    }
}
