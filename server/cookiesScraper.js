
const puppeteer = require('puppeteer');
// const pluginStealth = require('puppeteer-extra-plugin-stealth');
const {strava_login_url} = require("./stravaUrls.js")
// puppeteer.use(pluginStealth());
const fs = require('fs');



async function connectToStrava(email, password, uid) {
    let result={
        status: 400,
        message: "OK"
    }

    try{
    const browser = await puppeteer.launch({ headless: true });
    const pageLogin = await browser.newPage();
    await pageLogin.goto(strava_login_url, {waitUntil: 'load'});
    await pageLogin.waitForSelector('nav a')
    await pageLogin.click('nav a')
    await pageLogin.waitForSelector('.btn-accept-cookie-banner')
    await pageLogin.click('.btn-accept-cookie-banner')


    await pageLogin.waitForSelector('#email')
    await pageLogin.type('#email', email, { delay: 200 })
    await pageLogin.waitForSelector('#password')
    await pageLogin.type('#password', password, { delay: 300 })
    await pageLogin.click("#login-button")


    await pageLogin.waitForSelector('.avatar-badge')

  
        

    const cookies = await pageLogin.cookies()
    //console.log(cookies)
    pageLogin.close()
    browser.close()

    fs.writeFileSync("./cookies/"+uid+'_strava-cookies-session.json', JSON.stringify(cookies, null, 2))
    result.status=200
    result.message="Connected"


    }catch(error){
        result.status=500
        result.message=error
    }

    //console.log(result)
     return result
}

module.exports={
    connectToStrava
}

//connectToStrava("milan1andjelovic@gmail.com","123456789","w7r0oVmCZjYvrvipkMWJsa9FjbO2")