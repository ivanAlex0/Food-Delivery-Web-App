import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button} from "react-bootstrap";
import {addRestaurant, fetchZones} from "../../api/adminAPI";
import Select from "react-select";

function get(key) {
    let admin_info = localStorage.getItem(key);
    return JSON.parse(admin_info);
}

function AddRestaurant() {
    const navigate = useNavigate();
    const [admin = {
        adminId: '',
        email: '',
        restaurant: ''
    }, setAdmin] = useState(get('admin-info'));
    const [restaurant, setRestaurant] = useState({
        name: '',
        location: '',
        locationZone: {
            id: ''
        }
    });
    const [zones, setZones] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!admin)
            navigate('/admin/register')

        fetchZones()
            .then(response => {
                setZones(response)
            })
            .catch(error => {
                setError(error.response.data.message)
            });
    }, []);

    function handleChange(event) {
        const {name, value} = event.target;
        setRestaurant(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
    }

    function handleSelect(selected) {
        setRestaurant(prevState => {
            return {
                ...prevState,
                'locationZone': selected.value
            };
        })
    }

    async function handleSubmit() {
        addRestaurant(admin.adminId, restaurant)
            .then(() => {
                navigate('/admin/login');
            })
            .catch(error => {
                setError(error.response.data.message)
            })
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
            <br/>

            <input
                name={'location'}
                type={'text'}
                placeholder={'location'}
                onChange={handleChange}
            />
            <br/>

            <Select
                options={
                    zones.map((zone) => {
                        return {
                            value: {
                                id: zone.id
                            },
                            label: zone.name
                        }
                    })
                }
                onChange={handleSelect}
            >
            </Select>

            <br/>
            <br/>

            <br/>
            <Button onClick={handleSubmit}>Add restaurant, boss!</Button>

            <h1>{error}</h1>
        </div>
    );
}

export default AddRestaurant;