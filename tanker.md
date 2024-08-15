# Hva skal vi lage

Vi skal lage medling om vedtak for behandling

1) Motta signal fra frontend med en behandlingsId
2) Hente korrespondernde behandling fra dp-behandling 
3) Hva slags melding skal vi lage, basert på opplysningstreet fra dp behandling 
4) Hente ut meldingsblokkene til denne meldingen fra sanity 
5) Gå gjennom meldingsblokkene for å finne tekniske ider
6) og hent ut korresponderende opplysningsverdier fra opplysningstre fra dp-behandling
7) Returner blokkid med dictionary med tekniskId og opplysningsverdier

-> saksbehandler sier OK
-> saksbehandler legger på firtekst
8) Frontend kaller dp-melding-om-vedtak med komplett ett eller annet som vi kan lage brev av
