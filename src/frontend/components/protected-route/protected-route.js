import React from 'react';
import { Route, Redirect } from 'react-router-dom';

const ProtectedRoute = function ({ logedIn, ...props }) {
    if (logedIn)
        return <Route { ...props } />;
    else {
        return <Redirect to='/login' />;
    }
}

export default ProtectedRoute;