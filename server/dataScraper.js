const puppeteer = require('puppeteer-extra');
// const pluginStealth = require('puppeteer-extra-plugin-stealth');
// puppeteer.use(pluginStealth());
const fs = require('fs');
const {strava_login_url} = require("./stravaUrls.js")



async function scrapeUserData(uid,monthYear) {
    let response={
        status:200,
        message: "Downloaded user data",
        activities: []
    }

    try{
    const cookiesString = fs.readFileSync("./cookies/"+uid+'_strava-cookies-session.json', 'utf8');
    const cookies = JSON.parse(cookiesString);

    process.on('unhandledRejection', (reason, p) => {
	console.error('Unhandled Rejection at: Promise', p, 'reason:', reason);
    response.status=500
    response.message='Unhandled Rejection at: Promise' + reason
    });

    
    const browser = await puppeteer.launch({ headless: true });
    const page = await browser.newPage();
    await page.setCookie.apply(page, cookies);
    await page.goto(strava_login_url, {waitUntil: 'load'});

    response.activities = await scrapeUserActivities(page,monthYear,cookies,browser)


    browser.close()
    }catch(error){
        //console.log(error)
        response.status=500
        response.message=error
    }
    return response
}

async function scrapeUserActivities(page,monthYear,cookies,browser) {
    
    await page.evaluate(async () => {
        let arrow = document.querySelectorAll(".app-icon.icon-caret-down.icon-dark")[2]
        await arrow.click()
        
        setTimeout(async () => {
                await document.querySelectorAll(".options.open-menu li a")[1].click()
        }, 2000);
     })
     await page.waitForSelector('.training-activity-row')


     const activityCountString = await page.evaluate(()=>{
        return document.querySelector(".activity-count")? document.querySelector(".activity-count").textContent : "0"
     })

     const activityCountStrings = activityCountString.split(" ")

     const activitiesPerPage = 20

     let pageCount = Math.ceil(parseInt(activityCountStrings[0])/activitiesPerPage)

     if(pageCount===0) return []
     let finished=false
     let trainingsToFilter = []
     let trainings
    
    do{
        //console.log("Page "+ pageCount+" opened")
         trainings = await page.$$eval(".training-activity-row", (trainingRows) => { 
            let activities=[]
            trainingRows.forEach(trainingRow => {
                let rowData={}
                rowData["type"] = trainingRow.querySelector(".view-col.col-type").textContent
                rowData["date"] = trainingRow.querySelector(".view-col.col-date").textContent
                rowData["link"] = trainingRow.querySelector(".view-col.col-title a").href
                activities.push(rowData)
            }) 
            return activities
        })
        if(checkStartEnd(trainings,monthYear)){
            //console.log("Funtion returned true for continue")
            trainingsToFilter.push.apply(trainingsToFilter, trainings)
        }else{
            //console.log("Funtion returned false for continue")
            finished=true
        }

        pageCount--
        if(pageCount==0){
            //console.log("Colected all pages. Exiting process")
            finished=true
        }
        
        if(pageCount>0){
            await page.evaluate(async () => {
                let arrow = document.querySelector(".btn.btn-default.btn-sm.next_page")
                await arrow.click()
            })
            await page.waitForSelector('.training-activity-row')
        }

    }while(!finished)

    let filteredArray = await filterAllNotEqualDates(trainingsToFilter,monthYear,cookies,browser)
    page.close()
    return filteredArray
}

async function filterAllNotEqualDates(trainings,monthYear,cookies,browser){
    let trainingsToReturn = []
    let monthYearParts = monthYear.split("/")
    let yearToCheck = parseInt(monthYearParts[1])
    let monthToCheck = parseInt(monthYearParts[0])
    let trainingParts 
    let trainingYear,trainingMonth
    let trainingData
    for(var i=0; i < trainings.length; i++){
        if(trainings[i].type == 'Run'){
            trainingParts = trainings[i].date.split(",")
            trainingParts = trainingParts[1].split("/")
            trainingMonth = parseInt(trainingParts[0])
            trainingYear = parseInt(trainingParts[2])
            if(trainingYear === yearToCheck){
                if(trainingMonth === monthToCheck){
                    trainingData = await getPostData(trainings[i].link,cookies,browser)
                    trainingsToReturn.push(trainingData)
                }
            }
        }

    }
    return trainingsToReturn
}

function checkStartEnd(trainings,monthYear){
    let firstDateParts = trainings[0].date.split(",")
    let secondDateParts = trainings[trainings.length-1].date.split(",")

    firstDateParts = firstDateParts[1].split("/")
    secondDateParts = secondDateParts[1].split("/")

    for (var i = 0; i < firstDateParts.length; i++) {
        firstDateParts[i] = parseInt(firstDateParts[i]) 
    }

    for (var i = 0; i < secondDateParts.length; i++) {
        secondDateParts[i] = parseInt(secondDateParts[i]) 
    }

   let firstYear,firstMonth,secondYear,secondMonth

   if(firstDateParts[2]===secondDateParts[2]){
         firstYear = firstDateParts[2]
         secondYear = secondDateParts[2]
         if(firstDateParts[0]===secondDateParts[0]){
            firstMonth = firstDateParts[0]
            secondMonth = secondDateParts[0]
         }else if(firstDateParts[0] < secondDateParts[0]){
            firstMonth = firstDateParts[0]
            secondMonth = secondDateParts[0]
         }else{
            secondMonth = firstDateParts[0]
            firstMonth = secondDateParts[0]
         }
   }else if(firstDateParts[2] < secondDateParts[2]){
        firstYear = firstDateParts[2]
        secondYear = secondDateParts[2]
        firstMonth = firstDateParts[0]
        secondMonth = secondDateParts[0]
   }else{
    firstYear = secondDateParts[2]
    secondYear = firstDateParts[2]
    secondMonth = firstDateParts[0]
    firstMonth = secondDateParts[0]
   }

   let monthYearParts = monthYear.split("/")
   let yearToCheck = parseInt(monthYearParts[1])
   let monthToCheck = parseInt(monthYearParts[0])




   if(yearToCheck > secondYear){
        return false
   }else if(yearToCheck === secondYear){
                if(monthToCheck > secondMonth){
                    return false
                }else{
                    return true
                }     
   }else{
        return true
   }
}


async function scrapeMonthlyActivities(uid,monthlyLink) {
    let response={
        status:200,
        message: "Downloaded activities",
        activities: []
    }
   
    try{
    const cookiesString = fs.readFileSync("./cookies/"+uid+'_strava-cookies-session.json', 'utf8');
    const cookies = JSON.parse(cookiesString);

    process.on('unhandledRejection', (reason, p) => {
	console.error('Unhandled Rejection at: Promise', p, 'reason:', reason);
    response.status=500
    response.message='Unhandled Rejection at: Promise' + reason
    });


    if(response.status!=200) return response

   
        const browser = await puppeteer.launch({ headless: true });
        const page = await browser.newPage();
        await page.setCookie.apply(page, cookies);
        await page.goto(monthlyLink, {waitUntil: 'load'})
        try {
            await page.waitForNavigation({ timeout: 10000 })
          } catch (e) {
              //console.log("Navigation 8 sec passed")
          }

        response.activities = await getAthletePosts(page,cookies,browser)
        browser.close()
    }
    catch(error){
        //console.log(error)
        response.status=500
        response.message=error
    }

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

     //console.log("LINKS:")
    // console.log(postLinks.length)
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
        let parsedDate = dictionary["date"].split(" on ")
        if(parsedDate.length>1){
            dictionary["date"] ="\n"+parsedDate[1]
        }
        return dictionary
    })

    

    data["type"]="Run"

    page.close()
    return data
}


module.exports={scrapeUserData,scrapeMonthlyActivities}



