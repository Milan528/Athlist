
const puppeteer = require('puppeteer-extra');
const pluginStealth = require('puppeteer-extra-plugin-stealth');
const {strava_login_url} = require("./stravaUrls.js")
puppeteer.use(pluginStealth());
const fs = require('fs');



async function connectToStrava(email, password, uid) {

    const browser = await puppeteer.launch({ headless: false });
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

    //await pageLogin.waitForNavigation()
    //await pageLogin.reload({ waitUntil: ["networkidle0", "domcontentloaded"] });
    await pageLogin.waitForSelector('.avatar-badge')

   // setTimeout(() => {
        

    const cookies = await pageLogin.cookies()
    console.info("cookies are ", cookies);
    pageLogin.close()
    browser.close()

   // }, 2000);

    // let result = await fs.writeFile(uid+'_strava-cookies-session.json', JSON.stringify(cookies, null, 2), function(err) {
    //      if (err) return -1
    //      return 0 
    //  });

     let result={
         status: 400,
         message: "Msg"
     }
     try{
        fs.writeFileSync(uid+'_strava-cookies-session.json', JSON.stringify(cookies, null, 2))
        result.status=200
        result.message="Connected"
     }catch(error){
        result.status=500
        result.message=error
     }

     return result
}

module.exports={
    connectToStrava
}

//connectToStrava("milan1andjelovic@gmail.com","123456789","8MMCH0Cc5tWUWyl6GmukrQfzk993")