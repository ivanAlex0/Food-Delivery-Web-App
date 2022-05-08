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

async function loginToken(accountDTO) {
    const params = new URLSearchParams()
    params.append('username', accountDTO.credential)
    params.append('password', accountDTO.password)
    const response = await axios(
        {
            method: 'POST',
            url: 'http://localhost:8080/api/login',
            params: params
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

async function addRestaurant(adminId, restaurant, accessToken) {
    const response = await axios(
        {
            method: 'post',
            url: path + 'addRestaurant',
            data: JSON.stringify(restaurant),
            params: {
                id: adminId
            },
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken
            }
        });
    return await response.data;
}

async function addFood(category, food, accessToken) {
    const response = await axios(
        {
            method: 'post',
            url: path + 'addFood',
            data: JSON.stringify(food),
            params: {
                categoryId: category.categoryId
            },
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken

            }
        });
    return await response.data;
}

async function fetchMenu(restaurant, accessToken) {
    const response = await axios(
        {
            method: 'get',
            url: path + 'fetchMenu',
            params: {
                restaurantId: restaurant.restaurantId
            },
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken
            }
        });
    return await response.data;
}

async function changeStatus(order, accessToken) {
    const response = await axios(
        {
            method: 'POST',
            url: path + 'changeStatus',
            params: {
                orderId: order.orderId,
                status: order.state.orderStatus
            },
            headers: {
                'Authorization': 'Bearer ' + accessToken
            }
        }
    );
    return await response.data;
}

async function fetchOrders(restaurant, accessToken) {
    const response = await axios(
        {
            method: 'get',
            url: path + 'fetchOrders',
            params: {
                restaurantId: restaurant.restaurantId
            },
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken
            }
        });
    return await response.data;
}

async function refreshToken(refreshToken) {
    const response = await axios(
        {
            method: 'get',
            url: path + 'refreshToken',
            headers: {
                'Authorization': 'Bearer ' + refreshToken
            }
        });
    return await response.data;
}

async function generatePDF(admin, accessToken) {
    const response = await axios({
        method: 'get',
        url: path + 'generatePDF',
        headers:{
            'Authorization': 'Bearer ' + accessToken
        },
        params: {
            'adminId': admin.adminId
        }
    })
    return await response.data;
}

export {
    generatePDF,
    refreshToken,
    fetchZones,
    addRestaurant,
    loginToken,
    sendLogin,
    addFood,
    fetchMenu,
    sendRegister,
    changeStatus,
    fetchOrders,
    path
}