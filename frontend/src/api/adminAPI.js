import axios from "axios";

async function fetchZones(setZones) {
    const response = await axios.get('http://localhost:8080/api/admin/fetchZones');
    const zones = await response.data.response;
    setZones(zones);
}

async function addRestaurant(adminId, restaurant)
{
    console.log(JSON.stringify(restaurant))
    return await axios({
        method: 'post',
        url: 'http://localhost:8080/api/admin/addRestaurant',
        data: JSON.stringify(restaurant),
        params: {
            id: adminId
        },
        headers: {
            'Content-Type': 'application/json'
        }
    }).catch((error) => {
        return error.response.data
    })
}

export {fetchZones, addRestaurant}