import axios from "axios";

const path = 'http://localhost:8080/api/customer/';

export async function sendLogin(accountDTO) {
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

export async function sendRegister(customer) {
    console.log(customer)
    const response = await axios(
        {
            method: 'POST',
            url: path + 'register',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            data: JSON.stringify(customer),
        });
    return await response.data;
}

export async function fetchRestaurants(accessToken) {
    const response = await axios(
        {
            method: 'GET',
            url: path + 'fetchRestaurants',
            headers: {
                'Authorization': 'Bearer ' + accessToken
            }
        });
    return await response.data;
}

export async function sendOrder(order, customerId, restaurantId, details, accessToken) {
    const response = await axios(
        {
            method: 'POST',
            url: path + 'placeOrder',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken
            },
            data: JSON.stringify(order),
            params: {
                customerId: customerId,
                restaurantId: restaurantId,
                details: details
            }
        }
    );
    return await response.data;
}

export async function fetchOrders(customerId, accessToken) {
    const response = await axios(
        {
            method: 'GET',
            url: path + 'fetchOrders',
            params: {
                customerId: customerId
            },
            headers: {
                'Authorization': 'Bearer ' + accessToken
            }
        }
    );
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

