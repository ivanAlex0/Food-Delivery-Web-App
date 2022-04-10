import axios from "axios";

const path = 'http://localhost:8080/api/admin/';

async function sendLogin(accountDTO) {
    const response = await axios(
        {
            method: 'POST',
            url: path + 'auth',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            data: JSON.stringify(accountDTO)
        });
    return await response.data;
}

async function sendRegister(accountDTO) {
    const response = await axios(
        {
            method: 'POST',
            url: path + 'register',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            data: JSON.stringify(accountDTO),
        });
    return await response.data;
}

async function fetchZones() {
    const response = await axios({
            method: 'GET',
            url: path + 'fetchZones'
        }
    );
    return await response.data.response;
}

async function addRestaurant(adminId, restaurant) {
    const response = await axios(
        {
            method: 'post',
            url: path + 'addRestaurant',
            data: JSON.stringify(restaurant),
            params: {
                id: adminId
            },
            headers: {
                'Content-Type': 'application/json'
            }
        });
    return await response.data;
}

async function addFood(category, food) {
    const response = await axios(
        {
            method: 'post',
            url: path + 'addFood',
            data: JSON.stringify(food),
            params: {
                categoryId: category.categoryId
            },
            headers: {
                'Content-Type': 'application/json'
            }
        });
    return await response.data;
}

async function fetchMenu(restaurant) {
    const response = await axios(
        {
            method: 'get',
            url: path + 'fetchMenu',
            params: {
                restaurantId: restaurant.restaurantId
            },
            headers: {
                'Content-Type': 'application/json'
            }
        });
    return await response.data;
}

async function changeStatus(order) {
    const response = await axios(
        {
            method: 'POST',
            url: path + 'changeStatus',
            params: {
                orderId: order.orderId,
                status: order.state.orderStatus
            }
        }
    );
    return await response.data;
}

async function fetchOrders(restaurant) {
    const response = await axios(
        {
            method: 'get',
            url: path + 'fetchOrders',
            params: {
                restaurantId: restaurant.restaurantId
            },
            headers: {
                'Content-Type': 'application/json'
            }
        });
    return await response.data;
}

export {fetchZones, addRestaurant, sendLogin, addFood, fetchMenu, sendRegister, changeStatus, fetchOrders, path}