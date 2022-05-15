const puppeteer = require('puppeteer-extra');
const pluginStealth = require('puppeteer-extra-plugin-stealth');
puppeteer.use(pluginStealth());
const fs = require('fs');
const {writeToDatabase} = require("./firebaseClient.js")
const {extractIdFromUrl,extractPostTypeFromText} = require("./stringFunctions.js");




async function scrapeAthleteLogin(url) {
    const cookiesString = fs.readFileSync('./strava-cookies-session.json', 'utf8');
    const cookies = JSON.parse(cookiesString);

    process.on('unhandledRejection', (reason, p) => {
	console.error('Unhandled Rejection at: Promise', p, 'reason:', reason);
    });

    const browser = await puppeteer.launch({ headless: false });
    //const browser = await puppeteer.launch();
    

    let data=await scrapeAthlete(url, browser , cookies)
    console.log(data)
    data.athlete["url"]=url
    writeToDatabase(data)
    return "Data is being uploaded to database"
    //console.log(athleteData)
    //return { name, avatarURL }
}

async function scrapeAthlete(url, browser, cookies) {
    const page = await browser.newPage();
    await page.setCookie.apply(page, cookies);
    await page.goto(url, {waitUntil: 'load'});

    const userData = await page.evaluate(() => {
        var dictionary = {};
        dictionary["name"] = document.querySelectorAll(".athlete-name")[1].textContent
        if(document.querySelector(".location")!=null){
            dictionary["location"] = document.querySelector(".location").textContent
        }else{
            dictionary["location"] = "Unknown"
        }
        dictionary["imageURL"] = document.querySelector(".avatar-content img").src
        dictionary["following"] = document.querySelectorAll(".inline-stats li a")[0].textContent
        dictionary["followers"] = document.querySelectorAll(".inline-stats li a")[1].textContent
        dictionary["last4WeeksActivities"] = document.querySelectorAll(".text-display5").textContent
        return dictionary

     })
    

    let keyArray = [
        "avgRunDistance4Week", "avgRun4WeekTime", "avg4WeekRuns", "bestOneMile", "bestHalfMarathon", "bestMarathon", "allTimeRunDistance", "allTimeRuns"
    ]

    let valueLinkArray = [
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[2]/table/tbody[1]/tr[2]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[2]/table/tbody[1]/tr[3]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[2]/table/tbody[1]/tr[4]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[2]/table/tbody[3]/tr[3]/td[2]/a',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[2]/table/tbody[3]/tr[6]/td[2]/a',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[2]/table/tbody[3]/tr[7]/td[2]/a',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[2]/table/tbody[6]/tr[2]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[2]/table/tbody[6]/tr[3]/td[2]'
    ]

    let data = await getAthleteActivity('.running-tab', page, keyArray, valueLinkArray, userData)
    userData['runningData'] = data

    


    keyArray = [
        "avg4WeekRides", "avgCyclingDistance4Week", "avgCycling4WeekTime", "allTimeCyclingDistance", "allTimeRides", "longestRide", "biggestClimb"
    ]

    valueLinkArray = [
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[1]/table/tbody[1]/tr[2]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[1]/table/tbody[1]/tr[3]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[1]/table/tbody[1]/tr[4]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[1]/table/tbody[4]/tr[2]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[1]/table/tbody[4]/tr[3]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[1]/table/tbody[4]/tr[4]/td[2]/a',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[1]/table/tbody[4]/tr[5]/td[2]/a',

    ]

    data = await getAthleteActivity('.cycling-tab', page, keyArray, valueLinkArray, userData)
    userData['cyclingData'] = data

    


    keyArray = [
        "avg4WeekSwims", "avgDistance4Week", "avgSwim4WeekTime", "allTimeSwimDistance", "allTimeSwims"
    ]

    valueLinkArray = [
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[3]/table/tbody[1]/tr[2]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[3]/table/tbody[1]/tr[3]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[3]/table/tbody[1]/tr[4]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[3]/table/tbody[4]/tr[2]/td[2]',
        '/html/body/div[2]/div[3]/div/div[3]/div[2]/div[3]/div[3]/table/tbody[4]/tr[3]/td[2]'
    ]

    data = await getAthleteActivity('.swimming-tab', page, keyArray, valueLinkArray, userData)
    userData['swimmingData'] = data;

    //postovi
    let postsArray=await getAthletePosts(page)
    let postsIdArray=[]
    let postID
    let postData;
       for(let i=0;i<postsArray.length;i++){
            postData=await scrapePost(postsArray[i].link,browser,cookies)
           // console.log(postData)
           // postData["url"]=postsArray[i].link
            postID=extractIdFromUrl(postsArray[i].link)
            postsIdArray[i]=postID
            postsArray[i]["postData"]=postData
         }
     //console.log(postsArray)



    // segmenti
    let segmentsArray=await getKomsAndCrs(page)
    let segmentData
    let segmentID
    let segmentsIdArray=[]
    for(let i=0;i<segmentsArray.length;i++){
        if(segmentsArray[i].type=="Run"){
            console.log("Calling scrape run segment")
            segmentData=await scrapeRunSegment(segmentsArray[i].link,browser,cookies)
        }else if(segmentsArray[i].type=="Swim"){
            console.log("Calling scrape swim segment")
            segmentData=await scrapeSwimSegment(segmentsArray[i].link,browser,cookies)
        }else if(segmentsArray[i].type=="Ride"){
            console.log("Calling scrape ride segment")
            segmentData=await scrapeRideSegment(segmentsArray[i].link,browser,cookies)
        }else{
            segmentData={}
            console.log("Segment type unknown")
        }
        segmentsArray[i]["segmentData"]=segmentData
        segmentID=extractIdFromUrl(segmentsArray[i].link)
        segmentsIdArray[i]=segmentID
    }


   var athleteData={}
   athleteData["athlete"]=userData;
   athleteData["segments"]=segmentsArray;
   athleteData["posts"]=postsArray;
   athleteData.athlete["postsId"]=postsIdArray
   athleteData.athlete["segmentsId"]=segmentsIdArray
   //console.log(athleteData)

   
   return athleteData
}

async function getKomsAndCrs(page) {
    const komsUrl = await page.evaluate(() => {
        return document.querySelectorAll(".tab")[3].href
    })
    await page.goto(komsUrl);

    const tableSize = await page.evaluate(() => {
        var table = document.querySelectorAll(".my-segments tbody tr td")
        return table.length
    })
     let rowsCount=tableSize/7
     let komsData=[]

     for(let i=0;i<rowsCount;i++){

        let data = await page.evaluate((i) => {
          let rowData={}
          rowData["type"] = document.querySelectorAll(".my-segments tbody tr td")[i*7].textContent
          rowData["name"] = document.querySelectorAll(".my-segments tbody tr td a")[i*2].textContent
          rowData["link"] = document.querySelectorAll(".my-segments tbody tr td a")[i*2].href 
          return rowData
        },i)

       komsData[i]=data;
     }
     return komsData
     
}


async function getAthletePosts(page){
   
    const recentPostsCount = await page.evaluate(() => {
        var count = document.querySelectorAll(".ActivityEntryBody--activity-name--AGMA5 a")
        return count.length
    })
    let posts=[]

     for(let i=0;i<recentPostsCount;i++){

        let data = await page.evaluate((i) => {
          let postData={}
         // postData["type"] = document.querySelectorAll(".Activity--entry-icon--RlkFx svg")[i] ne radi
          postData["name"] = document.querySelectorAll(".ActivityEntryBody--activity-name--AGMA5 a")[i].textContent
          postData["link"] = document.querySelectorAll(".ActivityEntryBody--activity-name--AGMA5 a")[i].href 
          return postData
        },i)

        posts[i]=data;
     }
     //console.log(posts)
     return posts
}

async function scrapePost(url, browser, cookies) {
    const page = await browser.newPage();
    await page.setCookie.apply(page, cookies);
    await page.goto(url, {waitUntil: 'load'});

    const postTypeText = await  page.evaluate(() => {
        return document.querySelector(".title").textContent
    })
    const postType = extractPostTypeFromText(postTypeText)



   
    let data={
        postData : {
            type : "Unknown"
        }
    }
    if(postType=="Swim"){
        console.log("Calling scrape swim post")
        return await scrapeSwimPost(page)
    }else if(postType=="Run"){
        console.log("Calling scrape run post")
       return await scrapeRunPost(page)
    }else if(postType=="Ride"){
        console.log("Calling scrape ride post")
        return await scrapeRidePost(page)
    }else{
        console.log("Unknown post type")
        return data
    }

}

async function scrapeRunPost(page){
    console.log("Scraping Run POST")
    const data = await page.evaluate(() => {
        var dictionary = {};
        dictionary["postName"] = document.querySelector(".text-title1.marginless.activity-name").textContent
        dictionary["postDistance"] = document.querySelectorAll(".inline-stats li strong")[0].textContent
        dictionary["postMovingTime"] = document.querySelectorAll(".inline-stats li strong")[1].textContent
        dictionary["postPace"] = document.querySelectorAll(".inline-stats.section li strong")[2].textContent
        dictionary["elevation"] = document.querySelectorAll(".spans3")[0].textContent
        dictionary["calories"] = document.querySelectorAll(".spans3")[1].textContent
        dictionary["elapsedTime"] = document.querySelectorAll(".spans3")[2].textContent
        return dictionary

    })


    const lapsFlag = await  page.evaluate(() => {
        const element = document.querySelectorAll(".pagenav li a")[2]
        if(element==null){
            return -1
        }else
         return 1  
    })

    console.log("LAPS FLAG IS: "+lapsFlag)
    let laps=[]
    if(lapsFlag==1){
        const lapsUrl = await page.evaluate(() => {
            return document.querySelectorAll(".pagenav li a")[2].href
        })
        await page.goto(lapsUrl);
    
        await page.reload({ waitUntil: ["networkidle0", "domcontentloaded"] });
        
        const tableSize = await page.evaluate(() => {
            var table = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")
            return table.length
        })
        //console.log("Table size " + tableSize)
        let rowsCount=Math.floor(tableSize/6)
        //console.log("Laps count "+ rowsCount)

        for(let i=0;i<rowsCount;i++){

            let lap = await page.evaluate((i) => {
            let rowData={}
            rowData["lap"] = i+1
            rowData["distance"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+1].textContent
            rowData["time"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+2].textContent 
            rowData["pace"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+3].textContent
            rowData["elev"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+4].textContent
            rowData["hr"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+5].textContent 
            return rowData
            },i)

            laps[i]=lap;
        }

    }
     data["laps"]=laps
     data["type"]="Run"
    // console.log(data)

    page.close()
    return data
}

async function scrapeSwimPost(page){
    
    await page.reload({ waitUntil: ["networkidle0", "domcontentloaded"] });
    console.log("Scraping Swim POST")
    const data = await page.evaluate(() => {
        var dictionary = {};
        dictionary["postName"] = document.querySelector(".text-title1.marginless.activity-name").textContent
        dictionary["postDistance"] = document.querySelectorAll(".inline-stats li strong")[0].textContent
        dictionary["postMovingTime"] = document.querySelectorAll(".inline-stats li strong")[1].textContent
        dictionary["postPace"] = document.querySelectorAll(".inline-stats.section li strong")[2].textContent
        dictionary["elevation"] = ""
        dictionary["calories"] = document.querySelectorAll(".spans3")[1].textContent
        dictionary["elapsedTime"] = document.querySelectorAll(".spans3")[0].textContent
        return dictionary

    })


    // const lapsFlag = await  page.evaluate(() => {
    //     const element = document.querySelectorAll(".pagenav li a")[2]
    //     if(element==null){
    //         return -1
    //     }else
    //      return 1  
    // })

    ///console.log("LAPS FLAG IS: "+lapsFlag)
    let laps=[]
    //if(lapsFlag==1)
        // const lapsUrl = await page.evaluate(() => {
        //     return document.querySelectorAll(".pagenav li a")[2].href
        // })
        // await page.goto(lapsUrl);
    
        // await page.reload({ waitUntil: ["networkidle0", "domcontentloaded"] });
        
        const tableSize = await page.evaluate(() => {
            var table = document.querySelectorAll("#efforts-table table tbody tr td")
            return table.length
        })
        console.log("Table size " + tableSize)
        let rowsCount=Math.floor(tableSize/5)
        console.log("Laps count "+ rowsCount)

        for(let i=0;i<rowsCount;i++){

            let lap = await page.evaluate((i) => {
            let rowData={}
            rowData["lap"] = i+1
            rowData["distance"] = document.querySelectorAll("#efforts-table table tbody tr td")[i*5+1].textContent
            rowData["time"] = document.querySelectorAll("#efforts-table table tbody tr td")[i*5+2].textContent 
            rowData["pace"] = document.querySelectorAll("#efforts-table table tbody tr td")[i*5+3].textContent
            rowData["hr"] = document.querySelectorAll("#efforts-table table tbody tr td")[i*5+4].textContent 
            return rowData
            },i)

            laps[i]=lap;
        }

    
     data["laps"]=laps
     data["type"]="Swim"
     console.log(data)

    page.close()
    return data
}


async function scrapeRidePost(page){
    console.log("Scraping Ride POST")
    const data = await page.evaluate(() => {
        var dictionary = {};
        dictionary["postName"] = document.querySelector(".text-title1.marginless.activity-name").textContent
        dictionary["postDistance"] = document.querySelectorAll(".inline-stats li strong")[0].textContent
        dictionary["postMovingTime"] = document.querySelectorAll(".inline-stats li strong")[1].textContent
        dictionary["elevation"] = document.querySelectorAll(".inline-stats.section li strong")[2].textContent
        dictionary["speedAvg"] = document.querySelectorAll(".unstyled tbody td")[0].textContent
        dictionary["speedMax"] = document.querySelectorAll(".unstyled tbody td")[1].textContent
       // dictionary["elapsedTime"] = document.querySelectorAll(".unstyled tbody td")[3].textContent
        return dictionary

    })

    const [el] = await page.$x('/html/body/div[2]/div[3]/section/div/div[1]/div[2]/div[1]/table/tbody[3]/tr/td');
    const value = await el.getProperty('textContent');
    data['elapsedTime'] = await value.jsonValue();

    


    const lapsFlag = await  page.evaluate(() => {
        const element = document.querySelectorAll(".pagenav li a")[2]
        if(element==null){
            return -1
        }else
         return 1  
    })

    ///console.log("LAPS FLAG IS: "+lapsFlag)
    let laps=[]
    if(lapsFlag==1){
        const lapsUrl = await page.evaluate(() => {
            return document.querySelectorAll(".pagenav li a")[2].href
        })
        await page.goto(lapsUrl);
    
        await page.reload({ waitUntil: ["networkidle0", "domcontentloaded"] });
        
        const tableSize = await page.evaluate(() => {
            var table = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")
            return table.length
        })
        //console.log("Table size " + tableSize)
        let rowsCount=Math.floor(tableSize/6)
        //console.log("Laps count "+ rowsCount)

        for(let i=0;i<rowsCount;i++){

            let lap = await page.evaluate((i) => {
            let rowData={}
            rowData["lap"] = i+1
            rowData["distance"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+1].textContent
            rowData["time"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+2].textContent 
            rowData["pace"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+3].textContent
            rowData["elev"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+4].textContent
            rowData["hr"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+5].textContent 
            return rowData
            },i)

            laps[i]=lap;
        }

    }
     data["laps"]=laps
     data["type"]="Ride"
    // console.log(data)

    page.close()
    return data
}



async function scrapeRunSegment(url, browser, cookies) {
    const page = await browser.newPage();
    await page.setCookie.apply(page, cookies);
    await page.goto(url, {waitUntil: 'load'});
    console.log("Scraping Run SEGMENT")


    const data = await page.evaluate(() => {
        var dictionary = {};
        dictionary["segmentName"] = document.querySelector("#js-full-name").textContent
        dictionary["segmentLocation"] = document.querySelector(".location").textContent
        dictionary["segmentDistance"] = document.querySelectorAll(".list-stats li b")[0].textContent
        dictionary["avgGrade"] = document.querySelectorAll(".list-stats li b")[1].textContent
        dictionary["lowestElev"] = document.querySelectorAll(".list-stats li b")[2].textContent
        dictionary["highestElev"] = document.querySelectorAll(".list-stats li b")[3].textContent
        dictionary["elevDifference"] = document.querySelectorAll(".list-stats li b")[4].textContent
        return dictionary

    })
    const keyArray = [
        "athleteName", "pace", "heartRate", "time"
    ]

    const valueLinkArray = [
        '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[2]/strong/a',
        '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[4]',
        '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[5]',
        '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[6]'
    ]

    const size = keyArray.length;
    for (let i = 0; i < size; i++) {
        await addDataToMap(data, keyArray[i], valueLinkArray[i], page);
    }

    page.close()
    return data
}

async function scrapeSwimSegment(url, browser, cookies) {
    const page = await browser.newPage();
    await page.setCookie.apply(page, cookies);
    await page.goto(url, {waitUntil: 'load'});
    console.log("Scraping Swim SEGMENT. Method not implemented")
    


    // const data = await page.evaluate(() => {
    //     var dictionary = {};
    //     dictionary["segmentName"] = document.querySelector("#js-full-name").textContent
    //     dictionary["segmentLocation"] = document.querySelector(".location").textContent
    //     dictionary["segmentDistance"] = document.querySelectorAll(".list-stats li b")[0].textContent
    //     dictionary["avgGrade"] = document.querySelectorAll(".list-stats li b")[1].textContent
    //     dictionary["lowestElev"] = document.querySelectorAll(".list-stats li b")[2].textContent
    //     dictionary["highestElev"] = document.querySelectorAll(".list-stats li b")[3].textContent
    //     dictionary["elevDifference"] = document.querySelectorAll(".list-stats li b")[4].textContent
    //     return dictionary

    // })
    // const keyArray = [
    //     "athleteName", "pace", "heartRate", "time"
    // ]

    // const valueLinkArray = [
    //     '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[2]/strong/a',
    //     '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[4]',
    //     '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[5]',
    //     '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[6]'
    // ]

    // const size = keyArray.length;
    // for (let i = 0; i < size; i++) {
    //     await addDataToMap(data, keyArray[i], valueLinkArray[i], page);
    // }

    page.close()
    return {}
}

async function scrapeRideSegment(url, browser, cookies) {
    const page = await browser.newPage();
    await page.setCookie.apply(page, cookies);
    await page.goto(url, {waitUntil: 'load'});
    console.log("Scraping Ride SEGMENT")


    const data = await page.evaluate(() => {
        var dictionary = {};
        dictionary["segmentName"] = document.querySelector("#js-full-name").textContent
        dictionary["segmentLocation"] = document.querySelector(".location").textContent
        dictionary["segmentDistance"] = document.querySelectorAll(".list-stats li b")[0].textContent
        dictionary["avgGrade"] = document.querySelectorAll(".list-stats li b")[1].textContent
        dictionary["lowestElev"] = document.querySelectorAll(".list-stats li b")[2].textContent
        dictionary["highestElev"] = document.querySelectorAll(".list-stats li b")[3].textContent
        dictionary["elevDifference"] = document.querySelectorAll(".list-stats li b")[4].textContent
        return dictionary

    })
    const keyArray = [
        "athleteName", "speed", "heartRate", "power" ,"time"
    ]

    const valueLinkArray = [
        '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[2]/strong/a',
        '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[4]',
        '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[5]',
        '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[6]',
        '/html/body/div[3]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr[1]/td[7]'
    ]

    const size = keyArray.length;
    for (let i = 0; i < size; i++) {
        await addDataToMap(data, keyArray[i], valueLinkArray[i], page);
    }

    page.close()
    return data
}

async function addDataToMap(map, key, valueLink, page) {
    const [el] = await page.$x(valueLink);
    if(el != null ){
        const value = await el.getProperty('textContent');
        map[key] = await value.jsonValue();
    }else{
        map[key] = ""
    }
}


async function scrapeRunSegmentLogin(url) {
    const cookiesString = fs.readFileSync('./strava-cookies-session.json', 'utf8');
    const cookies = JSON.parse(cookiesString);

    process.on('unhandledRejection', (reason, p) => {
	console.error('Unhandled Rejection at: Promise', p, 'reason:', reason);
    });

    const browser = await puppeteer.launch({ headless: false });
    
    await scrapeRunSegment(url, browser, cookies)
}


async function getAthleteActivity(linkToClick, page, keyArray, valueLinkArray) {
    var data = {};

   try{
        console.log("Trying to click tag: "+ linkToClick)
        await page.waitForSelector(linkToClick)
        await page.click(linkToClick, { delay: 5000 })
   }catch(e){
       console.log("Element already clicked")
   }


    const size = keyArray.length;
    for (let i = 0; i < size; i++) {
        await addDataToMap(data, keyArray[i], valueLinkArray[i], page);

    }
    return data

}

//testiranje za gmail
async function openGoogleTest(email, password) {
    const cookiesString = fs.readFileSync('./cookies-session.json', 'utf8');
    //console.log("cookiesString are ", cookiesString);
    const cookies = JSON.parse(cookiesString);
    //console.log("cookies are ", cookies);

    process.on('unhandledRejection', (reason, p) => {
	console.error('Unhandled Rejection at: Promise', p, 'reason:', reason);
    });

    const browser = await puppeteer.launch({headless: false });
    const page = await browser.newPage();
   
    
    console.info("setting cookies")
    await page.setCookie.apply(page, cookies);


    
    await page.goto('https://www.google.com/', {waitUntil: 'load'});

    const browser2 = await puppeteer.launch({ headless: false });
    const page2 = await browser2.newPage();
    await page2.goto('https://www.google.com/search?q=gmail&oq=gmail&aqs=chrome.0.69i59j35i39i362j69i59j69i61l2j69i60l3.782j0j7&sourceid=chrome&ie=UTF-8', {waitUntil: 'load'});

   

    //return browser

}

module.exports={scrapeAthleteLogin}

//scrapeAthleteLogin('https://www.strava.com/athletes/26934035')
//openGoogleTest()

/*
 const posts = await page.$$eval(".a-title a", (articles) => { //selector is first parameter 
            return articles.map(x => x.href) //src is web address of image
            // return articles.map(x => x.textContent) //src is web address of image
        })
*/
