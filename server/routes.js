const express = require("express");
const cors = require("cors");
const { scrapeUserData,scrapeMonthlyActivities } = require("./dataScraper.js")
const { connectToStrava } = require("./cookiesScraper.js")
const router = express();
router.use(cors());
router.use(express.urlencoded({extended: true}));
router.use(express.json())

router.post("/connectToStrava", async(req, res) =>{
   console.log("Received connection request from client: "+ req.body.uid)
   const data = await connectToStrava(req.body.email,req.body.password,req.body.uid)
   res.status(data.status).send(JSON.stringify(data))
})


router.post("/scrapeUserData", async(req, res) =>{
    console.log("Received scrape request from client: "+ req.body.uid)
    const data = await scrapeUserData(req.body.uid,req.body.date)
    res.status(data.status).send(JSON.stringify(data))
})

router.post("/scrapeMonthlyActivities", async(req, res) =>{
    console.log("Received scrape monthly activities request from client: "+ req.body.uid)
    const data = await scrapeMonthlyActivities(req.body.uid,req.body.link)
    res.status(data.status).send(JSON.stringify(data))
})

// router.get('/', (req, res) => {
//     res.send("Server funkcionise");
//   });

//za localhost
router.listen(4000, () => {
    console.log(`Server started on port: 4000`);
})

//za domain
// router.listen()


// const hostname = '127.0.0.1';
// const port = 3000;


// router.listen(port, hostname, () => {
//   console.log(`Server running at http://${hostname}:${port}/`);
// });

