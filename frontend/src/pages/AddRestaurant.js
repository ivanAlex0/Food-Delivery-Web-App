import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button} from "react-bootstrap";
import {addRestaurant, fetchZones} from "../api/adminAPI";

function get(key) {
    let admin_info = localStorage.getItem(key);
    return JSON.parse(admin_info);
}

function AddRestaurant() {
    const navigate = useNavigate();
    const admin = get('admin-info');
    const [errorMessage, setErrorMessage] = useState('');
    const [restaurant, setRestaurant] = useState({
        'name': '',
        'location': '',
        'locationZone': {
            'id': ''
        }
    });
    const [zones, setZones] = useState([]);

    useEffect(() => {
        fetchZones(setZones)
    }, []);

    function handleChange(event) {
        const {name, value} = event.target;
        setRestaurant(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
        console.warn(restaurant)
    }

    function handleSelect(event) {
        const {name, value} = event.target;
        setRestaurant(prevState => {
            return {
                ...prevState,
                'locationZone': JSON.parse(value)
            };
        })
        console.warn(restaurant)
    }

    async function handleSubmit() {
        let response = await addRestaurant(admin.adminId, restaurant)
        if (!response.message)
            console.warn("YAY")
        else setErrorMessage(response.message)
    }

    return (
        <div>
            <h1>Add you restaurant, chef!</h1>
            <input
                name={'name'}
                type={'text'}
                placeholder={'name'}
                onChange={handleChange}
            />

            <input
                name={'location'}
                type={'text'}
                placeholder={'location'}
                onChange={handleChange}
            />

            <select
                name={'locationZone'}
                onChange={handleSelect}
                defaultValue={''}>
                {
                    zones.map((zone) => {
                            let val = {id: zone.id};
                            return <option value={JSON.stringify(val)} key={zone.id}>
                                {zone.name}
                            </option>
                        }
                    )
                }
            </select>

            <br/>
            <Button onClick={handleSubmit}>Add restaurant, boss!</Button>

            <h1>{errorMessage}</h1>
        </div>
    );
}

export default AddRestaurant;