const puppeteer = require('puppeteer-extra');
const pluginStealth = require('puppeteer-extra-plugin-stealth');
puppeteer.use(pluginStealth());
const fs = require('fs');
const {strava_login_url} = require("./stravaUrls.js")



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

async function scrapeUserRunActivity(monthlyActivityLink,uid){
        const cookiesString = fs.readFileSync(uid+'_strava-cookies-session.json', 'utf8');
        const cookies = JSON.parse(cookiesString);

        process.on('unhandledRejection', (reason, p) => {
        console.error('Unhandled Rejection at: Promise', p, 'reason:', reason);
        });

        const browser = await puppeteer.launch({ headless: false });
        const page = await browser.newPage();
        await page.setCookie.apply(page, cookies);
        await page.goto(monthlyActivityLink, {waitUntil: 'load'});


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
    
         data["type"]="Run"
         console.log(data)
    
        page.close()
        return data
}


async function scrapeMonthlyActivities(uid,monthlyLink) {
    let response={
        status:200,
        message: "Downloaded activities",
        activities: []
    }
    //fali try catch
    const cookiesString = fs.readFileSync(uid+'_strava-cookies-session.json', 'utf8');
    const cookies = JSON.parse(cookiesString);

    process.on('unhandledRejection', (reason, p) => {
	console.error('Unhandled Rejection at: Promise', p, 'reason:', reason);
    response.status=500
    response.message='Unhandled Rejection at: Promise' + reason
    });



    if(response.status!=200) return response

    try{
        const browser = await puppeteer.launch({ headless: false });
        const page = await browser.newPage();
        await page.setCookie.apply(page, cookies);
        await page.goto(monthlyLink, {waitUntil: 'load'})
        try {
            await page.waitForNavigation({ timeout: 10000 })
          } catch (e) {
              console.log("Navigation 8 sec passed")
          }

        response.activities = await getAthletePosts(page,cookies,browser)
        browser.close()
    }
    catch(error){
        console.log(error)
        response.status=500
        response.message=error
    }
    //console.log(response.activities)
    return response
   
}


async function getAthletePosts(page,cookies,browser){

 const postLinks = await page.$$eval(".MediaBody--media-body--uT-hq h3 a", (posts) => { 
    let linksArray=[]
    posts.forEach(post=>{
        if(post.textContent.includes("Run") || post.textContent.includes("run")){
            linksArray.push(post.href)
    }  
        })
            return linksArray     
    })

    console.log("LINKS:")
    console.log(postLinks.length)
    let postsData=[]
    let data
   
    
    for(const link of postLinks){
        data = await getPostData(link,cookies,browser)
        postsData.push(data)
    }

    return postsData
}


async function getPostData(link,cookies,browser){

    console.log("Getting post data for "+link)
    const page = await browser.newPage();
    await page.setCookie.apply(page, cookies);
    await page.goto(link, {waitUntil: 'load'});

    const data = await page.evaluate(async () => {
        function getElementTextContent(selector, index=0){
            let el = document.querySelectorAll(selector)[index]
            return el? el.textContent : ""
        }

        var dictionary = {};


        dictionary["title"] = getElementTextContent(".text-title1.marginless.activity-name")
        dictionary["distance"] = getElementTextContent(".inline-stats li strong",0)
        dictionary["movingTime"] = getElementTextContent(".inline-stats li strong",1)
        dictionary["pace"] = getElementTextContent(".inline-stats.section li strong",2)
        dictionary["elevation"] = getElementTextContent(".spans3")
        dictionary["calories"] = getElementTextContent(".spans3",1)
        dictionary["elapsedTime"] = getElementTextContent(".spans3",2)
        dictionary["date"] = getElementTextContent(".details > time")
       
        return dictionary
    })

    data["type"]="Run"
    page.close()
    return data
}


module.exports={scrapeUserData,scrapeMonthlyActivities}
//11 postova za minut
//46 za 3 minuta            https://www.strava.com/athletes/26934035#interval?interval=202106&interval_type=month&chart_type=miles&year_offset=0
//scrapeMonthlyActivities("8MMCH0Cc5tWUWyl6GmukrQfzk993","https://www.strava.com/athletes/26934035#interval?interval=202108&interval_type=month&chart_type=miles&year_offset=0")
//scrapeUserData("8MMCH0Cc5tWUWyl6GmukrQfzk993")
//scrapeUserRunActivity("https://www.strava.com/activities/7205067652","28051997")


