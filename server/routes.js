const express = require("express");
const cors = require("cors");
const { readAllAthletes,readAllAthletePosts,readAllAthleteSegments } = require("./firebaseClient.js")
const { scrapeAthleteLogin } = require("./dataScraper.js")
const router = express();
router.use(cors());
router.use(express.urlencoded({extended: true}));
router.use(express.json())


router.get("/getAllAthletes", async(req, res) => {
    const data = await readAllAthletes();
    //res.send("u adresi ukucaj /proizvodi");
     res.json(data)
})

router.post("/getAthletePosts", async(req, res) => {
    const data = await readAllAthletePosts(req.body.ID)
    res.json(data)

})

router.post("/getAthleteSegments", async(req, res) => {
    const data = await readAllAthleteSegments(req.body.ID)
    res.json(data)

})

router.post("/scrapeAthlete", async(req, res) => {
    const data = await scrapeAthleteLogin(req.body.url)
    res.send(data)

})


router.listen(4000, () => {
    console.log(`Server started on port: 4000`);
})


/*  
        *RUN IN TERMINAL*

        npm i express mysql cors
        npm install -g nodemon //only once
		nodemon "fileName.js"
    */