function reverseString(str) {
    // Step 1. Use the split() method to return a new array
    var splitString = str.split(""); // var splitString = "hello".split("");
    // ["h", "e", "l", "l", "o"]
 
    // Step 2. Use the reverse() method to reverse the new created array
    var reverseArray = splitString.reverse(); // var reverseArray = ["h", "e", "l", "l", "o"].reverse();
    // ["o", "l", "l", "e", "h"]
 
    // Step 3. Use the join() method to join all elements of the array into a string
    var joinArray = reverseArray.join(""); // var joinArray = ["o", "l", "l", "e", "h"].join("");
    // "olleh"
    
    //Step 4. Return the reversed string
    return joinArray; // "olleh"
}


function extractIdFromUrl(url){
    let reverseUrl = reverseString(url)

    const urlParts = reverseUrl.split("/")
    let index=0
    while (index<urlParts.length && urlParts[index]=="") {
        index++
      }

   return reverseString(urlParts[index])

}

function extractPostTypeFromText(text){
    let reverse = reverseString(text)

    const parts = reverse.split("–")
    let index=0
    while (index<parts.length && parts[index]=="") {
        index++
      }
    let invisilbeLetters = reverseString(parts[index])
    let returnString=""
    for(let i=1;i<invisilbeLetters.length-1;i++){
        returnString+=invisilbeLetters[i]
    }

   return returnString
}


function extractDateFromString(text){
    const myArray = text.split(" ");
    return myArray[1]
}


module.exports={
    extractIdFromUrl,
    extractPostTypeFromText,
    extractDateFromString
}
    
