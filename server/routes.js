const express = require("express");
const cors = require("cors");
const { scrapeUserData,scrapeUserRunActivity } = require("./dataScraper.js")
const { connectToStrava } = require("./cookiesScraper.js")
const router = express();
router.use(cors());
router.use(express.urlencoded({extended: true}));
router.use(express.json())

router.post("/connectToStrava", async(req, res) =>{
   const data = await connectToStrava(req.body.email,req.body.password,req.body.uid)
   res.status(data.status).send(JSON.stringify(data))
})


router.post("/scrapeUserData", async(req, res) =>{
    console.log("Scraping for id: "+req.body.uid)
    const data = await scrapeUserData(req.body.uid)
    res.status(data.status).send(JSON.stringify(data))
})

router.post("/scrapeUserRunActivity", async(req, res) =>{
    const data = await scrapeUserRunActivity(req.body.link,req.body.uid)
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