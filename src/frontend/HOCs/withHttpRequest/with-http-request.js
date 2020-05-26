import React from 'react';
import { ApplicationContext } from '../../app';

export default function (Component) {
    return class UsesHttpComponent extends React.Component {

        state = {
            preventRendering: false
        }

        errorHandler = (handler) => {
            return function (error) {
                if (error === -1) return;

                handler(error);
            }
        }

        httpRequest = async (url, method, data) => {
            let requestInfo = {
                method,
                credentials: 'include',
                headers: {}
            };

            if (data) {
                requestInfo.headers = { 'Content-Type': 'application/json' };
                requestInfo.body = JSON.stringify(data);
            }

            let accesToken = localStorage.getItem('authorization');

            if (accesToken) {
                requestInfo.headers['Authorization'] = accesToken;
            }

            const request = new Request(url, requestInfo);

            console.log('sending request: ', request);
            console.log('response recived: ');

            let response;
            let responseData;
            try {
                response = await fetch(request);
                responseData = await response.json();
            }
            catch (_) { responseData = { code: 400, message: 'Ha ocurrido un error' }; }

            accesToken = response.headers.get('Authorization');
            if (accesToken)
                localStorage.setItem('authorization', accesToken);

            if (response.ok) {
                console.log(responseData);
                return responseData;
            }
            else {
                if (url != '/api/login' && (response.status === 403 || response.status === 401)) {
                    this.onUnAuthorized(true);
                    throw -1;
                }

                console.error(responseData);
                throw responseData;
            }
        }

        assignHandlerAndRender = (context) => {
            this.onUnAuthorized = context.onUnAuthorized;

            const { children, ...props } = this.props;

            return (
                <Component
                    httpRequest={this.httpRequest}
                    errorHandler={this.errorHandler}
                    {...props}
                >
                    {children}
                </Component>
            );
        }

        render() {

            if (this.state.preventRendering) return null;

            return <ApplicationContext.Consumer>
                {this.assignHandlerAndRender}
            </ApplicationContext.Consumer>
        }
    };
}
