import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Redirect, Switch, NavLink } from "react-router-dom";
import StudentForm from '../../Containers/StudentForm/StudentForm';
import StudentList from '../../Containers/StudentList/StudentList';
import Style from './index.module.scss'

export default class Portal extends Component {
    render() {
        return (
            <Router>
                <div className={Style.portalRoot}>
                    <div className={Style.leftSide}>
                        <NavLink to="/student">学员列表</NavLink>
                        <NavLink to="/student/new">新增学员</NavLink>
                    </div>
                    <div className={Style.rightSide}>
                        <Switch>
                            <Redirect exact from="/" to="/student" />
                            <Route exact path="/student" component={StudentList} />
                            <Route exact path="/student/new" component={StudentForm} />
                        </Switch>
                    </div>
                </div>
            </Router>
        )
    }
}


