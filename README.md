# RecipeDB

RecipeDB tarjoaa REST API:n reseptien, raaka-aineiden ja avainsanojen tallentamiseen ja hakuun tietokannasta. Toteutettu Jersey frameworkilla.

Sovellus on harjoitustyö Jyväskylän yliopiston kurssille SOA and Cloud Computing.

Työn ensimmäinen vaihe on toteutettu ryhmätyönä. Oma vastuuni oli mm. sovelluksen arkkitehtuurin suunnittelu, osa service-luokista sekä tiedon persistointi.

Toisessa vaiheessa työhön lisättiin autentikointi ja autorisointi. Tässä esitetty ratkaisu on oma toteutukseni, joka poikkeaa hieman ryhmätyön palautetusta versiosta.

Sovellus on julkaistuna osoitteessa https://soa-recipedb.herokuapp.com/webapi

Sovellukselle on yksinkertainen testikäyttöliittymä osoitteessa https://soa-recipedb.herokuapp.com/ 

Testikäyttöliittymä tarjoaa toiminnallisuuden tagien, raaka-aineiden ja reseptien lisäämiseen.

### Pikainen kuvaus API:sta:

Suurin osa toiminnoista vaatii käyttäjän tunnistautumisen. Osa toiminnoista vaatii admin-oikeudet.

**/auth**

Hyväksyy POST-pyynnön käyttäjän autentikointiin.

Pyynnön Authorization -otsakkeessa oltava Base64-enkoodatut tunnukset, esim. "Basic am9obmxlbm5vbjEyMzoxMjM="

Vastauksen Authorization -otsake sisältää JWT-tokenin jota käytetään myöhempien pyyntöjen autentikointiin lisäämällä pyynnön otsakkeeseen: Authorization: Bearer \<token here\>

**/accounts**

Hyväksyy POST-pyynnön uuden käyttäjätilin luomiseksi. Admin-käyttäjälle mahdollisia myös mm. GET ja DELETE pyynnöt.

Pyyntö muotoa:
```
{
    "name":"atomic-testerman",
    "password":"123123"
}
```
**/users**

Täältä pääsee käsiksi käyttäjäprofiileihin. Käyttäjätilin lisäksi käyttäjän on luotava profiili, joka tapahtuu POST-pyynnöllä:
```
{
    "username":"Atomic Testerman"
}
```
GET-pyyntö listaa kaikkien profiilien tiedot (vain admin).

**/users/{id}**

Palauttaa yksittäisen käyttäjän tiedot

**/recipes**

GET-pyyntö listaa kaikki reseptit.

POST-pyyntö luo uuden reseptin:
```
{
    "name":"pizza margherita",
    "portions": 2,
    "ingredients" : [{
        "ingredient": {
        "id": 2,
        "name": "wheat flour",
        "portionPrice": 1.5,
        "portionSize": 1.0,
        "unit": "kg"
    },
    "amount": "0.3"},
    {
        "ingredient": {
        "id": 8,
        "name": "cheese",
        "portionPrice": 4.5,
        "portionSize": 500.0,
        "unit": "grams"
    },
    "amount": "0.5"},
    {
        "ingredient": {
        "id": 10,
        "name": "canned tomatoes",
        "portionPrice": 0.8,
        "portionSize": 500.0,
        "unit": "grams"
    },"amount": "0.5"}],
    "tags": [    {
        "id": 4,
        "name": "#italian"
    },{
        "id": 7,
        "name": "#main course"
    }
    ]
}
```

Reseptin lisäämisen toteutus on aikataulusyistä johtuen kömpelö. Reseptiin kuuluvien raaka-aineiden tietojen on vastattava tietokannasta löytyviä tietoja.

**/recipes/{id}**

Palauttaa yksittäisen reseptin tiedot GET-pyynnöllä. PUT tai DELETE pyynnöt mahdollisia reseptin luoneelle tai admin-oikeuksilla varustetulle käyttäjälle.

**/ingredients**

GET-pyyntö listaa kaikki raaka-aineet.

POST-pyynnöllä luodaan uusia raaka-aineita. Pyynnön on oltava muotoa:
```
{
    "name":"onion",
    "unit":"whole",
    "portionSize":"1",
    "portionPrice":"0.5"
}
```

**/ingredients/{id}**

Palauttaa yksittäisen raaka-aineen tiedot GET-pyynnöllä. PUT tai DELETE pyynnöt mahdollisia raaka-aineen luoneelle tai admin-oikeuksilla varustetulle käyttäjälle.

**/recipes/{id}/ingredients**

GET-pyyntö listaa kaikki kyseisen reseptin raaka-aineet.

POST-pyynnöllä lisätään reseptiin uusia raaka-aineita. Pyynnön on oltava muotoa:

```
{
        "ingredient": {
        "id": 2,
        "name": "wheat flour",
        "portionPrice": 1.5,
        "portionSize": 1.0,
        "unit": "kg"
    },
    "amount": "0.3"
}
```    
**/recipes/{id}/ingredients/{id}**

Palauttaa yksittäisen raaka-aineen tiedot GET-pyynnöllä. PUT tai DELETE pyynnöt mahdollisia reseptin luoneelle tai admin-oikeuksilla varustetulle käyttäjälle.
