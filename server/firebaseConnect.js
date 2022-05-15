const firebase = require("firebase")


const firebaseConfig = {
    apiKey: "AIzaSyD1dPcXfxDruSj9gdU8DcEjfTtJD2EMbHk",
    authDomain: "testproject-4ceaa.firebaseapp.com",
    databaseURL: "https://testproject-4ceaa-default-rtdb.firebaseio.com",
    projectId: "testproject-4ceaa",
    storageBucket: "testproject-4ceaa.appspot.com",
    messagingSenderId: "586646398379",
    appId: "1:586646398379:web:1941a44fc975ea4c9e5eee",
    databaseURL: "https://testproject-4ceaa-default-rtdb.firebaseio.com/"
  };
  
// Initialize Firebase
const app = firebase.initializeApp(firebaseConfig);

module.exports=firebase







