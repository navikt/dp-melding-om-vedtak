package no.nav.dagpenger.vedtaksmelding.portabletext

fun css(saksId: String): String {
    //language=CSS
    return """
            @media screen {
              /*https://aksel.nav.no/monster-maler/brev/visuelle-retningslinjer-for-brev*/
             .melding-om-vedtak {
               width: 595px; /* A4 bredde */
               height: 842px; /* A4 høyde */
               padding: 64px 64px 74px;
             
               box-shadow: var(--a-shadow-medium);
               background-color: white;
               overflow: auto;
             
               font-family: Source_Sans_3, sans-serif;
               font-size: 11px;
               line-height: 16px;
               font-weight: normal;
             }
        
            .melding-om-vedtak-header,
            .melding-om-vedtak-logo {
              margin-bottom: 48px;
            }
        
            .melding-om-vedtak-header p {
              font-size: 11px;
              line-height: 16px;
              font-weight: normal;
              margin: 0;
            }
        
            .melding-om-vedtak-opplysning-verdi {
              white-space: nowrap;
            }
        
            /* Bruker floats for å gjøre PDF generatoren fornøyd */
            .melding-om-vedtak-saksnummer-dato {
              overflow: hidden; /* Clear the floats */
            }
        
           .melding-om-vedtak-saksnummer-dato-left {
             float: left;
           }
        
           .melding-om-vedtak-saksnummer-dato-right {
             float: right;
           }
        
           .melding-om-vedtak-tekst-blokk {
             margin-bottom: 26px;
           }
        
          .melding-om-vedtak-tekst-blokk-first {
            margin-bottom: 0;
          }
        
        .melding-om-vedtak-tekst-blokk h1 {
          font-size: 16px;
          line-height: 20px;
          font-weight: bold;
          letter-spacing: 0.3px;
          margin: 0 0 26px 0;
        }
        
        .melding-om-vedtak-tekst-blokk h2,
        .melding-om-vedtak-tekst-blokk h3,
        .melding-om-vedtak-tekst-blokk h4 {
          line-height: 16px;
          font-weight: bold;
          margin: 0 0 6px 0;
        }
        
        .melding-om-vedtak-tekst-blokk h2 {
          font-size: 13px;
          letter-spacing: 0.25px;
        }
        
        .melding-om-vedtak-tekst-blokk h3 {
          font-size: 12px;
          letter-spacing: 0.2px;
        }
        
        .melding-om-vedtak-tekst-blokk h4 {
          font-size: 11px;
          letter-spacing: 0.1px;
        }
        
        .melding-om-vedtak-tekst-blokk p {
          font-size: 11px;
          line-height: 16px;
          font-weight: normal;
        }
        
        .melding-om-vedtak-signatur-container {
          margin-top: 32px;
        }
        
        .melding-om-vedtak-signatur {
          float: left;
          width: 50%;
          margin-top: 0;
        }    
            
            }

           @media print {
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
             
             .melding-om-vedtak-opplysning-verdi {
               white-space: nowrap;
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
            

            .melding-om-vedtak-tekst-blokk {
              margin-bottom: 26px;
            }
            
            .melding-om-vedtak-tekst-blokk-first {
              margin-bottom: 0;
            }
            
            .melding-om-vedtak-tekst-blokk h1,
            .melding-om-vedtak-tekst-blokk h2,
            .melding-om-vedtak-tekst-blokk h3,
            .melding-om-vedtak-tekst-blokk h4 {
                page-break-after: avoid;
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

             .melding-om-vedtak-signatur-container {
               margin-top: 32px;
             }
             
             .melding-om-vedtak-signatur {
               float: left;
               width: 50%;
             }
             
             ul, ol, li {
                 list-style-type: disc;
             }

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
                     content: 'Saksid: $saksId';
                     font-family: 'Source Sans 3', serif;
                     font-size: 9pt;
                     padding-bottom: 26px;
                     padding-left: 8px;
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
