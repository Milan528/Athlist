const puppeteer = require('puppeteer-extra');
const pluginStealth = require('puppeteer-extra-plugin-stealth');
puppeteer.use(pluginStealth());
const fs = require('fs');
const {strava_login_url} = require("./stravaUrls.js")
const { extractDateFromString } = require("./stringFunctions.js")


async function scrapeUserData(uid) {
    let response={
        status:200,
        message: "Downloaded user data",
        stravaProfile: undefined
    }
    const cookiesString = fs.readFileSync(uid+'_strava-cookies-session.json', 'utf8');
    const cookies = JSON.parse(cookiesString);

    process.on('unhandledRejection', (reason, p) => {
	console.error('Unhandled Rejection at: Promise', p, 'reason:', reason);
    response.status=500
    response.message='Unhandled Rejection at: Promise' + reason
    });

    try{
    const browser = await puppeteer.launch({ headless: false });
    const page = await browser.newPage();
    await page.setCookie.apply(page, cookies);
    await page.goto(strava_login_url, {waitUntil: 'load'});

    const userData = await page.evaluate(() => {
        var dictionary = {};
        dictionary["athleteName"] = document.querySelectorAll(".athlete-name")[1].textContent
        dictionary["imageURL"] = document.querySelector(".avatar-img").src
        dictionary["following"] = document.querySelectorAll(".list-stats.text-center li b")[0].textContent
        dictionary["followers"] = document.querySelectorAll(".list-stats.text-center li b")[1].textContent
        dictionary["activitiesCount"] = document.querySelectorAll(".list-stats.text-center li b")[2].textContent
        return dictionary

     })

     userData["activities"]= await scrapeUserActivities(page)
     response.stravaProfile=userData
     browser.close()
    }catch(error){
        console.log(error)
        response.status=500
        response.message=error
    }
    console.log(response)
   return response

}

async function scrapeUserActivities(page) {
    
    await page.evaluate(async () => {
        let arrow = document.querySelectorAll(".app-icon.icon-caret-down.icon-dark")[2]
        await arrow.click()
        
        setTimeout(async () => {
                await document.querySelectorAll(".options.open-menu li a")[1].click()
        }, 2000);
     })
     await page.waitForSelector('.training-activity-row')

    const trainings = await page.$$eval(".training-activity-row", (trainingRows) => { 
        let activities=[]
        trainingRows.forEach(trainingRow => {
            let rowData={}
            rowData["type"] = trainingRow.querySelector(".view-col.col-type").textContent
            rowData["date"] = trainingRow.querySelector(".view-col.col-date").textContent
            rowData["title"] = trainingRow.querySelector(".view-col.col-title").textContent
            rowData["time"] = trainingRow.querySelector(".view-col.col-time").textContent
            rowData["distance"] = trainingRow.querySelector(".view-col.col-dist").textContent
            rowData["elevation"] = trainingRow.querySelector(".view-col.col-elev").textContent
            rowData["link"] = trainingRow.querySelector(".view-col.col-title a").href

            activities.push(rowData)
        }) 
        return activities
    })

    let filteredArray = trainings.filter(function(e) { return e.type == 'Run' })
    page.close()
    return filteredArray
}

async function scrapeUserRunActivity(link,uid){
        const cookiesString = fs.readFileSync(uid+'_strava-cookies-session.json', 'utf8');
        const cookies = JSON.parse(cookiesString);

        process.on('unhandledRejection', (reason, p) => {
        console.error('Unhandled Rejection at: Promise', p, 'reason:', reason);
        });

        const browser = await puppeteer.launch({ headless: false });
        const page = await browser.newPage();
        await page.setCookie.apply(page, cookies);
        await page.goto(link, {waitUntil: 'load'});


        const data = await page.evaluate(() => {
            var dictionary = {};
            dictionary["name"] = document.querySelector(".text-title1.marginless.activity-name").textContent
            dictionary["distance"] = document.querySelectorAll(".inline-stats li strong")[0].textContent
            //dictionary["postMovingTime"] = document.querySelectorAll(".inline-stats li strong")[1].textContent
            dictionary["time"] = document.querySelectorAll(".details time").textContent
            dictionary["pace"] = document.querySelectorAll(".inline-stats.section li strong")[2].textContent
            dictionary["elevation"] = document.querySelectorAll(".spans3")[0].textContent
            dictionary["calories"] = document.querySelectorAll(".spans3")[1].textContent
            dictionary["time"] = document.querySelectorAll(".spans3")[2].textContent
            return dictionary
        })
    
    
        // const lapsFlag = await  page.evaluate(() => {
        //     const element = document.querySelectorAll(".pagenav li a")[2]
        //     if(element==null){
        //         return -1
        //     }else
        //      return 1  
        // })
    
        // console.log("LAPS FLAG IS: "+lapsFlag)
        // let laps=[]
        // if(lapsFlag==1){
        //     const lapsUrl = await page.evaluate(() => {
        //         return document.querySelectorAll(".pagenav li a")[2].href
        //     })
        //     await page.goto(lapsUrl);
        
        //     await page.reload({ waitUntil: ["networkidle0", "domcontentloaded"] });
            
        //     const tableSize = await page.evaluate(() => {
        //         var table = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")
        //         return table.length
        //     })
        //     //console.log("Table size " + tableSize)
        //     let rowsCount=Math.floor(tableSize/6)
        //     //console.log("Laps count "+ rowsCount)
    
        //     for(let i=0;i<rowsCount;i++){
    
        //         let lap = await page.evaluate((i) => {
        //         let rowData={}
        //         rowData["lap"] = i+1
        //         rowData["distance"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+1].textContent
        //         rowData["time"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+2].textContent 
        //         rowData["pace"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+3].textContent
        //         rowData["elev"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+4].textContent
        //         rowData["hr"] = document.querySelectorAll(".laps.marginless.padded-values tbody tr td")[i*6+5].textContent 
        //         return rowData
        //         },i)
    
        //         laps[i]=lap;
        //     }
    
        // }
        //  data["laps"]=laps
         data["type"]="Run"
         console.log(data)
    
        page.close()
        return data
}



module.exports={scrapeUserData,scrapeUserRunActivity}


//scrapeUserData("8MMCH0Cc5tWUWyl6GmukrQfzk993")
//scrapeUserRunActivity("https://www.strava.com/activities/7205067652","28051997")


/*
 const posts = await page.$$eval(".a-title a", (articles) => { //selector is first parameter 
            return articles.map(x => x.href) //src is web address of image
            // return articles.map(x => x.textContent) //src is web address of image
        })
*/
