import React, { Component } from 'react';
import { Link } from 'react-router-dom';

import { Navbar, NavbarBrand, NavbarToggle, NavbarCollapse } from 'react-bootstrap';

class Header extends Component {
    render() {
        return (
        <div>
		<nav className="user-menu">
			<a href="javascript:;" className="main-menu-access">
			<i className="icon-proton-logo"></i>
			<i className="icon-reorder"></i>
			</a>
		</nav>
		<section className="title-bar">
			<div className="logo">
				<h1><a href="index.html"><img src="src/assets/images/logo.png" alt="" />Cognizant</a></h1>
			</div>
			<div className="full-screen">
				<section className="full-top">
					<button id="toggle"><i className="fa fa-arrows-alt" aria-hidden="true"></i></button>	
				</section>
			</div>
			<div className="w3l_search">
				<form action="#" method="post">
					<input type="text" name="search" value="Search" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Search';}" required=""></input>
					<button className="btn btn-default" type="submit"><i className="fa fa-search" aria-hidden="true"></i></button>
				</form>
			</div>
			<div className="header-right">
				<div className="profile_details_left">
					<div className="header-right-left">
						<ul className="nofitications-dropdown">
							<li className="dropdown head-dpdn">
								<a href="#" className="dropdown-toggle" data-toggle="dropdown"><i className="fa fa-envelope"></i><span className="badge">3</span></a>
								<ul className="dropdown-menu anti-dropdown-menu w3l-msg">
									<li>
										<div className="notification_header">
											<h3>You have 3 new messages</h3>
										</div>
									</li>
									<li><a href="#">
									   <div className="user_img"><img src="src/assets/images/1.png" alt=""/></div>
									   <div className="notification_desc">
										<p>Lorem ipsum dolor amet</p>
										<p><span>1 hour ago</span></p>
										</div>
									   <div className="clearfix"></div>	
									</a></li>
									<li className="odd"><a href="#">
										<div className="user_img"><img src="src/assets/images/2.png" alt=""/></div>
									   <div className="notification_desc">
										<p>Lorem ipsum dolor amet </p>
										<p><span>1 hour ago</span></p>
										</div>
									  <div className="clearfix"></div>	
									</a></li>
									<li><a href="#">
									   <div className="user_img"><img src="src/assets/images/3.png" alt=""/></div>
									   <div className="notification_desc">
										<p>Lorem ipsum dolor amet </p>
										<p><span>1 hour ago</span></p>
										</div>
									   <div className="clearfix"></div>	
									</a></li>
									<li>
										<div className="notification_bottom">
											<a href="#">See all messages</a>
										</div> 
									</li>
								</ul>
							</li>
							<li className="dropdown head-dpdn">
								<a href="#" className="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i className="fa fa-bell"></i><span className="badge blue">3</span></a>
								<ul className="dropdown-menu anti-dropdown-menu agile-notification">
									<li>
										<div className="notification_header">
											<h3>You have 3 new notifications</h3>
										</div>
									</li>
									<li><a href="#">
										<div className="user_img"><img src="src/assets/images/2.png" alt=""/></div>
									   <div className="notification_desc">
										<p>Lorem ipsum dolor amet</p>
										<p><span>1 hour ago</span></p>
										</div>
									  <div className="clearfix"></div>	
									 </a></li>
									 <li className="odd"><a href="#">
										<div className="user_img"><img src="src/assets/images/1.png" alt=""/></div>
									   <div className="notification_desc">
										<p>Lorem ipsum dolor amet </p>
										<p><span>1 hour ago</span></p>
										</div>
									   <div className="clearfix"></div>	
									 </a></li>
									 <li><a href="#">
										<div className="user_img"><img src="src/assets/images/3.png" alt=""/></div>
									   <div className="notification_desc">
										<p>Lorem ipsum dolor amet </p>
										<p><span>1 hour ago</span></p>
										</div>
									   <div className="clearfix"></div>	
									 </a></li>
									 <li>
										<div className="notification_bottom">
											<a href="#">See all notifications</a>
										</div> 
									</li>
								</ul>
							</li>	
							<li className="dropdown head-dpdn">
								<a href="#" className="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i className="fa fa-tasks"></i><span className="badge blue1">15</span></a>
								<ul className="dropdown-menu anti-dropdown-menu agile-task">
									<li>
										<div className="notification_header">
											<h3>You have 8 pending tasks</h3>
										</div>
									</li>
									<li><a href="#">
										<div className="task-info">
											<span className="task-desc">Database update</span><span className="percentage">40%</span>
											<div className="clearfix"></div>	
										</div>
										<div className="progress progress-striped active">
											<div className="bar yellow colored_width_40"></div>
										</div>
									</a></li>
									<li><a href="#">
										<div className="task-info">
											<span className="task-desc">Dashboard done</span><span className="percentage">90%</span>
										   <div className="clearfix"></div>	
										</div>
										<div className="progress progress-striped active">
											 <div className="bar green colored_width_90"></div>
										</div>
									</a></li>
									<li><a href="#">
										<div className="task-info">
											<span className="task-desc">Mobile App</span><span className="percentage">33%</span>
											<div className="clearfix"></div>	
										</div>
									   <div className="progress progress-striped active">
											 <div className="bar red colored_width_33"></div>
										</div>
									</a></li>
									<li><a href="#">
										<div className="task-info">
											<span className="task-desc">Issues fixed</span><span className="percentage">80%</span>
										   <div className="clearfix"></div>	
										</div>
										<div className="progress progress-striped active">
											 <div className="bar  blue colored_width_33"></div>
										</div>
									</a></li>
									<li>
										<div className="notification_bottom">
											<a href="#">See all pending tasks</a>
										</div> 
									</li>
								</ul>
							</li>	
							<div className="clearfix"> </div>
						</ul>
					</div>	
					<div className="profile_details">		
						<ul>
							<li className="dropdown profile_details_drop">
								<a href="#" className="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
									<div className="profile_img">	
										<span className="prfil-img"><i className="fa fa-user" aria-hidden="true"></i></span> 
										<div className="clearfix"></div>	
									</div>	
								</a>
								<ul className="dropdown-menu drp-mnu">
									<li> <a href="#"><i className="fa fa-cog"></i> Settings</a> </li> 
									<li> <a href="#"><i className="fa fa-user"></i> Profile</a> </li> 
									<li> <a href="#"><i className="fa fa-sign-out"></i> Logout</a> </li>
								</ul>
							</li>
						</ul>
					</div>
					<div className="clearfix"> </div>
				</div>
			</div>
			<div className="clearfix"> </div>
		</section>
		</div>
        );
    }
}

export default Header;
