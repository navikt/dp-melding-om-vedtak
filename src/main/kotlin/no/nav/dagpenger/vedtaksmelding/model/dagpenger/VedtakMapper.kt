package no.nav.dagpenger.vedtaksmelding.model.dagpenger

class VedtakMapper(
    vedtakJson: String,
) {
    fun vedtak(): Vedtak {
        return Vedtak(
            behandlingId = opplysningData.behandlingId(),
            utfall = utfall(),
            opplysninger = this.build(),
        )
    }

    private val opplysningData = OpplysningData(vedtakJson)

    private fun utfall(): Vedtak.Utfall {
        return when {
            opplysningData.harRett() -> {
                Vedtak.Utfall.INNVILGET
            }

            else -> {
                Vedtak.Utfall.AVSLÅTT
            }
        }
    }

    private fun build(): Set<DagpengerOpplysning<*, *>> {
        val opplysningerFraData =
            buildSet<DagpengerOpplysning<*, *>> {
                this.add(DagpengerOpplysning.KravTilProsentvisTapAvArbeidstid(opplysningData))
                this.add(DagpengerOpplysning.InntektskravSiste12Måneder(opplysningData))
                this.add(DagpengerOpplysning.InntektskravSiste36Måneder(opplysningData))
                this.add(DagpengerOpplysning.ArbeidsinntektSiste12Måneder(opplysningData))
                this.add(DagpengerOpplysning.ArbeidsinntektSiste36Måneder(opplysningData))
                this.add(DagpengerOpplysning.AntallGSomGisSomGrunnlagVedVerneplikt(opplysningData))
                this.add(DagpengerOpplysning.BruktBeregningsregelGrunnlag(opplysningData))
                this.add(DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste6Måneder(opplysningData))
                this.add(DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste12Måneder(opplysningData))
                this.add(DagpengerOpplysning.HarBruktBeregningsregelArbeidstidSiste36Måneder(opplysningData))
                this.add(DagpengerOpplysning.UtbetaltArbeidsinntektPeriode1(opplysningData))
                this.add(DagpengerOpplysning.UtbetaltArbeidsinntektPeriode2(opplysningData))
                this.add(DagpengerOpplysning.UtbetaltArbeidsinntektPeriode3(opplysningData))
                this.add(DagpengerOpplysning.AntallStønadsukerSomGisVedOrdinæreDagpenger(opplysningData))
                this.add(
                    DagpengerOpplysning.AndelAvDagsatsMedBarnetilleggSomOverstigerMaksAndelAvDagpengegrunnlaget(
                        opplysningData,
                    ),
                )
                this.add(DagpengerOpplysning.BarnetilleggIKroner(opplysningData))
                this.add(DagpengerOpplysning.FørsteMånedAvOpptjeningsperiode(opplysningData))
                this.add(DagpengerOpplysning.SisteMånedAvOpptjeningsperiode(opplysningData))
                this.add(DagpengerOpplysning.SeksGangerGrunnbeløp(opplysningData))
                this.add(DagpengerOpplysning.Aldersgrense(opplysningData))
                this.add(DagpengerOpplysning.Grunnlag(opplysningData))
                this.add(DagpengerOpplysning.DagsatsMedBarnetilleggEtterSamordningOg90ProsentRegel(opplysningData))
                this.add(DagpengerOpplysning.Prøvingsdato(opplysningData))
                this.add(DagpengerOpplysning.FastsattVanligArbeidstidPerUke(opplysningData))
                this.add(DagpengerOpplysning.FastsattNyArbeidstidPerUke(opplysningData))
                this.add(DagpengerOpplysning.HarSamordnet(opplysningData))
                this.add(DagpengerOpplysning.SykepengerDagsats(opplysningData))
                this.add(DagpengerOpplysning.PleiepengerDagsats(opplysningData))
                this.add(DagpengerOpplysning.OmsorgspengerDagsats(opplysningData))
                this.add(DagpengerOpplysning.OpplæringspengerDagsats(opplysningData))
                this.add(DagpengerOpplysning.UføreDagsats(opplysningData))
                this.add(DagpengerOpplysning.ForeldrepengerDagsats(opplysningData))
                this.add(DagpengerOpplysning.SvangerskapspengerDagsats(opplysningData))
                this.add(DagpengerOpplysning.OppfyllerKravTilMinsteinntekt(opplysningData))
                this.add(DagpengerOpplysning.PeriodeSomGisVedVerneplikt(opplysningData))
                this.add(DagpengerOpplysning.Egenandel(opplysningData))
                this.add(DagpengerOpplysning.KravTilArbeidssøker(opplysningData))
                this.add(DagpengerOpplysning.OppfyllerKravTilMobilitet(opplysningData))
                this.add(DagpengerOpplysning.OppfyllerKravTilArbeidsfør(opplysningData))
                this.add(DagpengerOpplysning.OppfyllerKravTilArbeidssøker(opplysningData))
                this.add(DagpengerOpplysning.OppfyllerKravetTilEthvertArbeid(opplysningData))
                this.add(DagpengerOpplysning.OppyllerKravTilRegistrertArbeidssøker(opplysningData))
                this.add(DagpengerOpplysning.OppfyllerKravetTilIkkeUtestengt(opplysningData))
                this.add(DagpengerOpplysning.OppfyllerKravetTilOpphold(opplysningData))
                this.add(DagpengerOpplysning.IkkeFulleYtelser(opplysningData))
                this.add(DagpengerOpplysning.KravTilTapAvArbeidsinntektOgArbeidstid(opplysningData))
                this.add(DagpengerOpplysning.KravTilTaptArbeidstid(opplysningData))
                this.add(DagpengerOpplysning.KravTilTapAvArbeidsinntekt(opplysningData))
                this.add(DagpengerOpplysning.IkkeStreikEllerLockout(opplysningData))
                this.add(DagpengerOpplysning.KravTilAlder(opplysningData))
                this.add(DagpengerOpplysning.KravTilUtdanning(opplysningData))
                this.add(DagpengerOpplysning.OppfyllerMedlemskap(opplysningData))

                DagpengerOpplysning.GrunnlagetForVernepliktErHoyereEnnDagpengeGrunnlaget.fra(opplysningData)
                    ?.let { this.add(it) }
                DagpengerOpplysning.ErInnvilgetMedVerneplikt.fra(opplysningData)?.let { this.add(it) }
                DagpengerOpplysning.AntallPermitteringsuker.fra(opplysningData)?.let { this.add(it) }
                DagpengerOpplysning.AntallPermitteringsukerFisk.fra(opplysningData)?.let { this.add(it) }
                DagpengerOpplysning.OppfyllerKravetTilPermittering.fra(opplysningData)?.let { this.add(it) }
                DagpengerOpplysning.OppfyllerKravetTilPermitteringFiskeindustri.fra(opplysningData)?.let { this.add(it) }
                DagpengerOpplysning.AntallStønadsuker.fra(this).let { this.add(it) }
            }

        val deriverteOpplysninger =
            opplysningerFraData.filterIsInstance<DeriverbarOpplysning>().flatMap {
                it.deriverteOpplysninger
            }

        val opplysnings = opplysningerFraData + deriverteOpplysninger

        return opplysnings
    }
}
