import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {get} from "../../utils/utils";

function AdminHome() {
    const navigate = useNavigate();
    const [admin = {
        administrator: {},
        adminId: '',
        email: '',
        restaurant: {}
    }, setAdmin] = useState(get('admin-info'));

    useEffect(() => {
        if (!admin)
            navigate("/admin/login");
    }, [])

    return (
        <div>
            <button
                onClick={() => {
                    navigate('/admin/menu');
                }}>Menu Page
            </button>

            <button
                onClick={() => {
                }}>Orders Page
            </button>
        </div>
    );
}

export default AdminHome;