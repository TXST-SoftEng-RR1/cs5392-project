// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.5.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.5.0/firebase-analytics.js";
import { getFirestore, collection, doc, addDoc } from "https://www.gstatic.com/firebasejs/9.5.0/firebase-firestore.js"
import { getAuth, signInWithPopup, GoogleAuthProvider } from "https://www.gstatic.com/firebasejs/9.5.0/firebase-auth.js"
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries


    // Your web app's Firebase configuration
    // For Firebase JS SDK v7.20.0 and later, measurementId is optional
    const firebaseConfig = {
        apiKey: "AIzaSyBILj9C1r91uCSzTZxPHgiFEOsxuqmFuuk",
        authDomain: "ctl-checker.firebaseapp.com",
        projectId: "ctl-checker",
        storageBucket: "ctl-checker.appspot.com",
        messagingSenderId: "223322767389",
        appId: "1:223322767389:web:ff87ac196a249c172346f4",
        measurementId: "G-TJMH22ZF6H"
    };

    // Initialize Firebase
    const app = initializeApp(firebaseConfig);
    const analytics = getAnalytics(app);
    const auth = getAuth(app);
    const db = getFirestore(app);
    const provider = new GoogleAuthProvider();




// const docRef = await addDoc(collection(db, "users"), {
//     first: "Ada",
//     last: "Lovelace",
//     born: 1815
// });
// console.log("Document written with ID: ", docRef.id);

jQuery(document).ready(function() {
    let user;

    auth.onAuthStateChanged(function(user) {
       if (user) {
           let photoURL = user.photoURL;
           if (photoURL !== null) {
               let acctImgPreview = document.getElementById('myAcctImgPreview');
               let userProfilePic = document.getElementById('userProfilePic');
               acctImgPreview.height = 30;
               acctImgPreview.width = 30;
               acctImgPreview.src = photoURL;
               userProfilePic.src = photoURL;
               userProfilePic.height = 150;
               userProfilePic.width = 150;
           }
           document.getElementById('userName').innerHTML = user.displayName;
           document.getElementById('userEmail').placeholder = user.email;
           if (user.emailVerified) {
               console.log("User is verified.");
               document.getElementById('userEmailVerified').checked = true;
           } else {
               user.sendEmailVerification().then(function() {
                   // Email sent.
               }).catch(function(error) {
                   // An error happened.
               });
           }
       } else {
           // user is logged out
           document.getElementById('userAccountNavItem').classList.add("hidden");
           document.getElementById('userLogInNavItem').classList.remove("hidden");
       }
    });

    $("#signInBtn").click(function () {
        signInWithPopup(auth, provider)
            .then((result) => {
                // This gives you a Google Access Token. You can use it to access the Google API.
                const credential = GoogleAuthProvider.credentialFromResult(result);
                const token = credential.accessToken;
                // The signed-in user info.
                user = result.user;
                console.log("User: " + user.displayName);
                // User is signed in.
                document.getElementById('userAccountNavItem').classList.remove("hidden");
                document.getElementById('userLogInNavItem').classList.add("hidden");
            }).catch((error) => {
                // Handle Errors here.
                const errorCode = error.code;
                const errorMessage = error.message;
                // The email of the user's account used.
                const email = error.email;
                // The AuthCredential type that was used.
                const credential = GoogleAuthProvider.credentialFromError(error);
        });

        $("#signOutBtn").click(function () {
            auth.signOut()
                .then(function() {
                    // Sign-out successful.
                    console.log("sign out successful");
                })
                .catch(function(error) {
                    console.log("sign out error!");
                });
        });
    });
});