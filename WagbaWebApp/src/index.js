import {initializeApp} from 'https://www.gstatic.com/firebasejs/9.15.0/firebase-app.js';
import {getDatabase, onValue, ref, update} from 'https://www.gstatic.com/firebasejs/9.15.0/firebase-database.js';
const  firebaseConfig = {
    apiKey: "AIzaSyAAWDfWz_2cfNuMmSY8vK6Z8Y9lIeL7gkM",
    authDomain: "wagba-db.firebaseapp.com",
    databaseURL: "https://wagba-db-default-rtdb.firebaseio.com",
    projectId: "wagba-db",
    storageBucket: "wagba-db.appspot.com",
    messagingSenderId: "584155231982",
    appId: "1:584155231982:web:e04c5224a647e6039c25ff",
    measurementId: "G-N0FEEVKZYJ"
};

const app = initializeApp(firebaseConfig);
const db = getDatabase(app);
const restaurantRef = ref(db, 'Restaurant/');
//console.log(restaurantRef);
let restaurantNames = [];
let restaurantKey = [];
let usernames = [];
let usersphone = [];
let orders = [];
let restaurantOrdersRef = [];


// get list of restaurants from onValue listener
try {
    await onValue(restaurantRef, (snapshot) => {
        restaurantKey = [];
        restaurantNames = [];
        snapshot.forEach((childSnapshot) => {
            restaurantKey.push(childSnapshot.key);
            restaurantNames.push(childSnapshot.child("Name").val());
    });
    document.getElementById('restaurantsBtns').innerHTML = restaurantNames.map(restaurant => `<button class="btn btn-primary" type="button">${restaurant}</button>`).join('');
    });
}catch(error){
    console.log(error);
};

// set on click listener for each restaurant button if button is clicked, go to retrieve orders for that restaurant
document.getElementById('restaurantsBtns').addEventListener('click', (e) => {
    const restaurantId = getRestaurantId(e.target.innerHTML);
    var restaurantText= document.getElementById('restaurantText');
    restaurantText.innerHTML = e.target.innerHTML;
    setLogo(e.target.innerHTML);
    console.log(restaurantId);
    retrieveOrdersRef(restaurantId);
});

async function setLogo(restaurantName){
    const restaurantId = getRestaurantId(restaurantName);
    const restaurantLogoRef = ref(db, 'Restaurant/' + restaurantId + '/imgUrl');
    var logo = "";
    try{
        await onValue(restaurantLogoRef, (snapshot) => {
            logo = snapshot.val();
            if(logo === null){
                logo  = "https://i.imgur.com/g87q8gs.jpg" 
            }
            document.getElementById('restaurantLogo').src = logo;
        });
    }catch(error){
        console.log(error);
    };
};

var changeStatus = document.getElementById('changeStatusBtn');
changeStatus.onclick = function() {
    var orderStatus = document.getElementById('statusSelector');
    var orderId = document.getElementById('orderId').value;
    var newStatus = orderStatus.options[orderStatus.selectedIndex].value;
    const updates = {}
    updates['orders/' +orderId + '/orderStatus'] = newStatus
    update(ref(db), updates);
    alert("Order status changed to " + newStatus);
    const restaurantId = getRestaurantId(document.getElementById('restaurantName'))
    retrieveOrdersRef(restaurantId);
};

function getRestaurantId(restaurantName) {
    for (let i = 0; i < restaurantNames.length; i++) {
        if (restaurantNames[i] === restaurantName) {
            return restaurantKey[i];
        }
    }
}

// get list of orders from onValue listener
async function retrieveOrdersRef(restaurantId) {
    restaurantOrdersRef = [];
    const restaurantorderRef = ref(db, 'Restaurant/' + restaurantId + '/Orders/');
    try{
        await onValue(restaurantorderRef, (snapshot) => {
        restaurantOrdersRef = [];
        snapshot.forEach((childSnapshot) => {
            restaurantOrdersRef.push(childSnapshot.key);
            //console.log(childSnapshot.key);
            }); 
    });
    }catch(error){
        console.log(error);
    }
    retrieveOrders(restaurantOrdersRef);
    
}

async function retrieveOrders(restaurantOrdersRef) {
    orders = [];
    try
    {
        await restaurantOrdersRef.forEach((order) => {
        const orderRef = ref(db, 'orders/' + order);
        // real time check value of orderRef
        onValue(orderRef, (snapshot) => {
            const order = snapshot.val();
            orders.push(order);
            // refresh orders
        });
        });
    }
    catch(error)
    {
        console.log(error);
    }
    orders = orders.reverse();
    displayOrders(orders);
    
}

//function to display orders in div 'orders'
function displayOrders(orders) {
    getUpdatedUsersInfo(orders)
    //console.log("Orders: " + orders);
    if(orders.length === 0){
        document.getElementById('orders').innerHTML = `<h1>No orders yet</h1>
        <p>Please refresh restaurant</p>`;
    }else{
    document.getElementById('orders').innerHTML = orders.map((order, index) =>
    `<div class="card" style="width: 18rem;
    background-color: ${setBackGround(order.orderStatus)};
    ">
    <div class="card-body">
        <h5 class="card-title">Order: ${order.orderId}</h5>
        <p class="card-text">Username: ${usernames[index]}</p>
        <p class="card-text">User phone: ${usersphone[index]}</p>
        <p class="card-text">Time: ${order.time}</p>
        <p class="card-text">Status: ${order.orderStatus}</p>
        <p class="card-text">Dishes: <br>${displayDishes(order.dishes)}</p>
        <p class="card-text">Total: ${order.totalAmount}.</p>
        <p class="card-text">Gate: ${order.gate}</p>
    </div>
</div>`).join('');
}
}

function setBackGround(status){
    if (status === "placed") {
        return "#FF770099";
    } else if (status === "on delivery") {
        return "#FF110099";
    } else if (status === "delivered") {
        return "#4CAF5099";
    }
}
async function getUpdatedUsersInfo(orders) {
    usernames = [];
    usersphone = [];
    try{
        await orders.forEach((order) => {
            const userRef = ref(db, 'users/' + order.userId);
            onValue(userRef, (snapshot) => {
                const user = snapshot.val();
                updatedUsersInfo(user.username, user.phone)
            });
        });
    }catch(error){console.log(error);}
}
function updatedUsersInfo(username, userphone) {
    usernames.push(username);
    usersphone.push(userphone);
    //console.log(usernames, usersphone);
}

// display dishes as strings in order
function displayDishes(dishes) {
    let dishesInfo = "";
    //console.log(dishes);//
    dishes.forEach((dish) => {
            dishesInfo +="&emsp;" +  dish.name + " " + dish.price + "<br>";
            //console.log(dishesInfo);

    });
    return dishesInfo;
}




