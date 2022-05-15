const firebase = require("./firebaseConnect.js")
const {extractIdFromUrl} = require("./stringFunctions.js")



function writeToDatabase(data){
    console.log("FirebaseClient data:")
    console.log(data)

    writeAthleteData(data.athlete)
    writePostsData(data.posts)
    writeSegmentsData(data.segments)

}

function writeAthleteData(athleteData){
    let athleteID=extractIdFromUrl(athleteData.url)
    firebase.database().ref("athlete/"+athleteID).set(athleteData,(error) => {
        if (error) {
            console.log("There was an error while writing athlete "+athleteID)
        } else {
          console.log("Athlete "+athleteID+" saved")
        }
    })
}

function writePostsData(posts){
    for(let i=0;i<posts.length;i++){
        writePostData(posts[i])
    }
}

function writeSegmentsData(segments){
    for(let i=0;i<segments.length;i++){
        writeSegmentData(segments[i])
    }
}

function writePostData(post){
    let postID=extractIdFromUrl(post.link)
    firebase.database().ref("post/"+postID).set(post,(error) => {
        if (error) {
            console.log("There was an error while writing post "+postID)
        } else {
            console.log("Post "+postID+" saved")
        }
    })
}


function writeSegmentData(segment){
    let segmentID=extractIdFromUrl(segment.link)
    firebase.database().ref("segment/"+segmentID).set(segment,(error) => {
        if (error) {
            console.log("There was an error while writing segment "+segmentID)
        } else {
            console.log("Segment "+segmentID+" saved")
        }
    })
}





async function readAllAthletes(){
    const dbRef = firebase.database().ref();
    const snapshot = await  dbRef.child("athlete").get();
    // dbRef.child("athlete").get().then((snapshot) => { //stariji nacin ne ceka na snapshot nastavice dalje ovo mozda za callback bolje
    // if (snapshot.exists()) {
    //     let data=snapshot.val()
    //     data["ID"]=snapshot.key
    //     callback(data)
    // } else {
    //  console.log("No data available");
    //  }
    // }).catch((error) => {
    // console.error(error);
    // });

    if (snapshot.exists()) {
        let athleteArray=[]
        let index=0
        snapshot.forEach(child => {
            let data=child.val()
            data["ID"]=child.key
            athleteArray[index]=data
            index++
        }); 
       // console.log(athleteArray)
       return athleteArray
    } else {
         console.log("No data available");
         return {}
     }
}

async function readAllAthletePosts(athleteID){
    const dbRef = firebase.database().ref();
    const snapshot = await dbRef.child("athlete/"+athleteID+"/postsId").get();
    if (snapshot.exists()) {
        let postsArray=[]
        let data=snapshot.val()
        let postCount=data.length
       for(let i=0;i<postCount;i++){
        postsArray[i] = await readPost(data[i])
       }
       return postsArray
    } else {
     console.log("No data available");
     return {}
     }
    

    // dbRef.child("athlete/"+athleteID+"/postsId").get().then((snapshot) => {
    // if (snapshot.exists()) {
        
    //     let data=snapshot.val()
    //     let postCount=data.length
    //    for(let i=0;i<postCount;i++){
    //      readPost(data[i],callback,postCount)
    //    }
    //     //callback(data)
    // } else {
    //  console.log("No data available");
    //  }
    // }).catch((error) => {
    // console.error(error);
    // });
}

async function readPost(postID){
    const dbRef = firebase.database().ref();
    const snapshot = await dbRef.child("post/"+postID).get()
    if (snapshot.exists()) {
        let data=snapshot.val()
        data["ID"]=snapshot.key  
        return data
    } else {
        console.log("No data available");
        return {}
     }
    // dbRef.child("post/"+postID).get().then((snapshot) => {
    // if (snapshot.exists()) {
    //     let data=snapshot.val()
    //     data["ID"]=snapshot.key  
    // } else {
    //     console.log("No data available");
    //  }
    // }).catch((error) => {
       
    //     console.error(error);
       
    // });
    
}


async function readAllAthleteSegments(athleteID){
    const dbRef = firebase.database().ref();
    const snapshot = await dbRef.child("athlete/"+athleteID+"/segmentsId").get()
    if (snapshot.exists()) {
        let segmentsArray=[]
        let data=snapshot.val()
        let segmentCount=data.length
       for(let i=0;i<segmentCount;i++){
        segmentsArray[i] = await readSegment(data[i])
       }
       return segmentsArray
    } else {
        console.log("No data available");
        return {}
     }

    // dbRef.child("athlete/"+athleteID+"/segmentsId").get().then((snapshot) => {
    // if (snapshot.exists()) {
    //     let data=snapshot.val()
    //     let segmentCount=data.length
    //    for(let i=0;i<segmentCount;i++){
    //     readSegment(data[i],callback,segmentCount)
    //    }
    //     //callback(data)
    // } else {
    //  console.log("No data available");
    //  }
    // }).catch((error) => {
    // console.error(error);
    // });
}

async function readSegment(segtmentID){
    const dbRef = firebase.database().ref();
    const snapshot = await  dbRef.child("segment/"+segtmentID).get()
    if (snapshot.exists()) {
        let data=snapshot.val()
        data["ID"]=snapshot.key  
        return data
       // callback(data)
    } else {
        console.log("No data available");
        return {}
     }
    // dbRef.child("segment/"+segtmentID).get().then((snapshot) => {
    // if (snapshot.exists()) {
    //     let data=snapshot.val()
    //     data["ID"]=snapshot.key  
    //     addSegmentToGlobalArray(callback,segmentCount,data)
    //    // callback(data)
    // } else {
    //     console.log("No data available");
    //  }
    // }).catch((error) => {
       
    //     console.error(error);
       
    // });
    
}









//readAllAthletePosts('26934035',clbTest)
//readAllAthleteSegments('26934035',clbTest)
//readAllAthletes();
module.exports={writeToDatabase,readAllAthletes,readAllAthletePosts,readAllAthleteSegments}