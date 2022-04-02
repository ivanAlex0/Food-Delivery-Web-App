import React, {useEffect} from "react";
import {useNavigate} from "react-router-dom";

function get(key) {
    let admin_info = localStorage.getItem(key);
    return JSON.parse(admin_info);
}

function Admin() {
    const navigate = useNavigate();
    const admin = get('admin-info');


    useEffect(() => {
        if (!admin)
            navigate("/login");
        return undefined;
    }, [])

    return (
        <div>
            <h1> Hello, back </h1>
        </div>
    );
}

export default Admin;