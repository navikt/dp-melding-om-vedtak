package no.nav.dagpenger.vedtaksmelding.portabletext

fun css(): String {
    //language=CSS
    return """
        .melding-om-vedtak {
            font-family: 'Source Sans 3', sans-serif;
            font-size: 11pt;
            line-height: 16pt;
            font-weight: 400;
        }

        .melding-om-vedtak-header,
        .melding-om-vedtak-logo {
            margin-bottom: 48px;
        }

        .melding-om-vedtak-header p {
            font-size: 11pt;
            line-height: 16pt;
            font-weight: 400;
            margin: 0;
        }
        
        .melding-om-vedtak-saksnummer-dato {
            overflow: hidden; /* Clear the floats */
        }
        
        .melding-om-vedtak-saksnummer-dato-left {
            float: left;
        }
        .melding-om-vedtak-saksnummer-dato-right {
            float: right;
        }
        
        .melding-om-vedtak-opplysning-verdi {
          white-space: nowrap;
        }


          .melding-om-vedtak-tekst-blokk {
            margin-bottom: 26px;
          }
          
          .melding-om-vedtak-tekst-blokk-first {
            margin-bottom: 0;
          }

        .melding-om-vedtak-tekst-blokk h1 {
            font-size: 16pt;
            line-height: 20pt;
            font-weight: 700;
            margin: 0 0 26px 0;
            letter-spacing: 0.3px;
        }

        .melding-om-vedtak-tekst-blokk h2,
        .melding-om-vedtak-tekst-blokk h3,
        .melding-om-vedtak-tekst-blokk h4 {
            line-height: 16pt;
            font-weight: 700;
            margin: 0 0 6px 0;
        }

        .melding-om-vedtak-tekst-blokk h2 {
            font-size: 13pt;
            letter-spacing: 0.25px;
        }

        .melding-om-vedtak-tekst-blokk h3 {
            font-size: 12pt;
            letter-spacing: 0.2px;
        }

        .melding-om-vedtak-tekst-blokk h4 {
            font-size: 11pt;
            letter-spacing: 0.1px;
        }

        .melding-om-vedtak-tekst-blokk p {
            font-size: 11pt;
            line-height: 16pt;
            font-weight: 400;
        }

        .meldingOmVedtak__signatur-container {
          margin-top: 32px;
        }
        
        .meldingOmVedtak__signatur {
          float: left;
          width: 50%;
        
        }
        
        ul, ol, li {
            list-style-type: disc;
        }

        @page {
            padding-bottom: 26px;
            
            @bottom-right {
                content: 'side ' counter(page) ' av ' counter(pages);
                font-family: 'Source Sans 3', serif;
                font-size: 9pt;
                padding-bottom: 26px;
                padding-right: 8px;
            }

            @bottom-left {
                content: 'Saksid: TODO';
                font-family: 'Source Sans 3', serif;
                font-size: 9pt;
                padding-bottom: 26px;
                padding-left: 8px;
            }
        }

        @media print {
            .melding-om-vedtak-tekst-blokk h1,
            .melding-om-vedtak-tekst-blokk h2,
            .melding-om-vedtak-tekst-blokk h3,
            .melding-om-vedtak-tekst-blokk h4 {
                page-break-after: avoid;
            }
        }
        
        input {
            /* Resolves getControlFont IndexOutOfBoundsException */
            font-family: 'Source Sans 3', serif;
            font-size: 9pt;
        }
                
        svg {
          display: inline-block;
        }
        """.trimIndent()
}
