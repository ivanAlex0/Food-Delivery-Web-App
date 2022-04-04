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

export async function fetchRestaurants() {
    const response = await axios(
        {
            method: 'GET',
            url: path + 'fetchRestaurants',
        });
    return await response.data;
}

export default {sendRegister, path};