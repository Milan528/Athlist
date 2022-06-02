const firebase = require("firebase")

const firebaseConfig = {
  apiKey: "AIzaSyBHYegA-92d6-Cn-3uWcS85nlsWGRQvsoM",
  authDomain: "athlist-ba54f.firebaseapp.com",
  projectId: "athlist-ba54f",
  storageBucket: "athlist-ba54f.appspot.com",
  messagingSenderId: "1015162846485",
  appId: "1:1015162846485:web:9340893b57238133e4fd1c",
  databaseURL: "https://athlist-ba54f-default-rtdb.firebaseio.com/"
};
  
// Initialize Firebase
const app = firebase.initializeApp(firebaseConfig);

module.exports=firebase







