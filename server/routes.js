const express = require("express");
const cors = require("cors");
const { scrapeUserData,scrapeMonthlyActivities } = require("./dataScraper.js")
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
    console.log("Scraping for id: "+req.body.uid+" date: "+req.body.date)
    const data = await scrapeUserData(req.body.uid,req.body.date)
    res.status(data.status).send(JSON.stringify(data))
})

router.post("/scrapeMonthlyActivities", async(req, res) =>{
    const data = await scrapeMonthlyActivities(req.body.uid,req.body.link)
    res.status(data.status).send(JSON.stringify(data))
})

router.listen(4000, () => {
    console.log(`Server started on port: 4000`);
})

