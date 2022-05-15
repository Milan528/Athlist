
const puppeteer = require('puppeteer-extra');
const pluginStealth = require('puppeteer-extra-plugin-stealth');
puppeteer.use(pluginStealth());
const fs = require('fs');
const login_url_strava='https://www.strava.com/';
//const config = require('./config.json'); // contains username and password to use

// const delay = ms => new Promise(res => setTimeout(res, ms));

const login_url='https://www.google.com/';
console.info("login_url is ", login_url);


async function googleTEst() {
    const browser = await puppeteer.launch({headless: false});
    const page = await browser.newPage();
    await page.goto(login_url, {waitUntil: 'load'});
    console.log(page.url());
   
    let username='milan12andjelovic@gmail.com'
    let password='$$1lena$$'
    
    await page.click('.gb_1.gb_2.gb_9d.gb_9c')
  
    await page.waitForNavigation();
    await page.type('#identifierId',username,{ delay: 200 }); 
    
    await page.click('.VfPpkd-dgl2Hf-ppHlrf-sM5MNb button')
    await page.waitForNavigation({waitUntil: 'networkidle2'});
    await page.reload({ waitUntil: ["networkidle0", "domcontentloaded"] });
    await page.type('.Xb9hP input', password,{ delay: 300 });

    await page.click('.VfPpkd-dgl2Hf-ppHlrf-sM5MNb button');
    await page.waitForNavigation();

     const cookies = await page.cookies()
     console.info("cookies are ", cookies);

     fs.writeFile('cookies-session.json', JSON.stringify(cookies, null, 2), function(err) {
         if (err) throw err;
         console.log('completed write of cookies');
     });

    browser.close();
}


async function loginUserAndSaveCookies(email, password) {

    const browser = await puppeteer.launch({ headless: false });
    const pageLogin = await browser.newPage();
    await pageLogin.goto(login_url_strava, {waitUntil: 'load'});
    await pageLogin.waitForSelector('nav a')
    await pageLogin.click('nav a')
    await pageLogin.waitForSelector('.btn-accept-cookie-banner')
    await pageLogin.click('.btn-accept-cookie-banner')


    await pageLogin.waitForSelector('#email')
    await pageLogin.type('#email', email, { delay: 200 })
    await pageLogin.waitForSelector('#password')
    await pageLogin.type('#password', password, { delay: 300 })
    await pageLogin.click("#login-button")

    await pageLogin.waitForNavigation()

    const cookies = await pageLogin.cookies()
    console.info("cookies are ", cookies);

     fs.writeFile('strava-cookies-session.json', JSON.stringify(cookies, null, 2), function(err) {
         if (err) throw err;
         console.log('completed write of cookies');
     });

   // return browser

}
//googleTEst()
loginUserAndSaveCookies("milan1andjelovic@gmail.com","123456789")