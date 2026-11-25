package no.nav.dagpenger.vedtaksmelding.model.dagpenger

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class VedtakMapper(
    vedtakJson: String,
) {
    fun vedtak(): Vedtak =
        Vedtak(
            behandlingId = behandlingsresultatData.behandlingId(),
            utfall = behandlingsresultatData.utfall(),
            automatiskBehandling = behandlingsresultatData.automatiskBehandling(),
            opplysninger = this.build(),
            behandletHendelseType = behandlingsresultatData.behandletHendelseType(),
        )

    private val behandlingsresultatData = BehandlingsresultatData(vedtakJson)

    private fun MutableSet<DagpengerOpplysning<*, *>>.leggTilHvisVerdiFinnesPåVirkningsdato(func: () -> DagpengerOpplysning<*, *>) {
        try {
            this.add(func())
        } catch (e: BehandlingsresultatData.BehandlingResultatOpplysningIkkeFunnet) {
            logger.debug { "Opplysning ikke funnet: ${e.message} for behandlingId: ${behandlingsresultatData.behandlingId()}" }
        } catch (e: BehandlingsresultatData.PeriodeIkkeFunnet) {
            logger.debug { "Periode ikke funnet: ${e.message} for behandlingId: ${behandlingsresultatData.behandlingId()}" }
        }
    }

    private fun build(): Set<DagpengerOpplysning<*, *>> {
        val opplysningerFraData =
            buildSet {
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.InntektskravSiste12Måneder(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.InntektskravSiste36Måneder(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.TerskelFor12Måneder(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.ArbeidsinntektSiste12Måneder(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.ArbeidsinntektSiste36Måneder(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.BruktBeregningsregelGrunnlag(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.AntallBarnSomGirRettTilBarnetillegg(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.BarnetilleggIKroner(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.AntallUkerMedSanksjon(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.AntallSanksjonsdagerSomGjenstår(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.SisteMånedAvOpptjeningsperiode(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.SeksGangerGrunnbeløp(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.Aldersgrense(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.Grunnlag(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(behandlingsresultatData)
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.Virkningsdato(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.FastsattVanligArbeidstidPerUke(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.FastsattNyArbeidstidPerUke(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.HarSamordnet(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.SykepengerDagsats(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.PleiepengerDagsats(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OmsorgspengerDagsats(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OpplæringspengerDagsats(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.UføreDagsats(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.ForeldrepengerDagsats(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.SvangerskapspengerDagsats(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.PeriodeSomGisVedVerneplikt(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.Egenandel(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.EgenandelGjenstående(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.AntallStønadsdagerSomGjenstår(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.AntallPermitteringsdagerSomGjenstår(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.AntallDagerIEnUkePermittering(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.KravTilArbeidssøker(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OppfyllerKravTilMobilitet(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OppfyllerKravTilArbeidsfør(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OppfyllerKravTilArbeidssøker(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.ØnskerÅVæreRegistrertSomArbeidssøker(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OppfyllerMeldeplikt(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OppfyllerKravetTilOpphold(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.IkkeFulleYtelser(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.OppfyllerVilkåretOmTapAvArbeidstid(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.KravTilTapAvArbeidsinntekt(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.IkkeStreikEllerLockout(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.KravTilAlder(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.KravTilUtdanning(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OppfyllerMedlemskap(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.GodkjentLokalArbeidssøker(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.GodkjentKunDeltidssøker(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.ErIlagtSanksjonsperiodeVedSelvforskyldtArbeidsløshet(behandlingsresultatData)
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.GrunnlagetForVernepliktErHøyereEnnDagpengegrunnlaget(behandlingsresultatData)
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.SkalPermitteringVurderes(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.SkalPermitteringFiskeindustriVurderes(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.LovfestetFritaksperiodePermittering(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.AntallPermitteringsukerFisk(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.OppfyllerKravetTilPermittering(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.KravetTilAntallUkerFørGjenopptak(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.KravetTilAntallUkerArbeidForReberegningAvGrunnlag(behandlingsresultatData)
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.OppfyllerKravetTilReberegningAvGrunnlag(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.HarGittFullstendigeOgRiktigeOpplysninger(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato {
                    DagpengerOpplysning.HarGittAlleNødvendigeOpplysninger(
                        behandlingsresultatData,
                    )
                }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.LangDagpengeperiode(behandlingsresultatData) }
                this.leggTilHvisVerdiFinnesPåVirkningsdato { DagpengerOpplysning.KortDagpengeperiode(behandlingsresultatData) }
                DagpengerOpplysning.AntallStønadsuker.fra(this)?.let { this.add(it) }
                DagpengerOpplysning.MinstEllerUnder2GInntekt.fra(this)?.let { this.add(it) }
                DagpengerOpplysning.SisteDagMedRett.fra(behandlingsresultatData)?.let { this.add(it) }
                DagpengerOpplysning.GrunnlagErReberegnet.fra(behandlingsresultatData)?.let { this.add(it) }
            }

        val deriverteOpplysninger =
            opplysningerFraData.filterIsInstance<DeriverbarOpplysning>().flatMap {
                it.deriverteOpplysninger
            }

        val opplysninger = opplysningerFraData + deriverteOpplysninger

        return opplysninger
    }
}
