import React from 'react';
import './App.css';
import Navbar from './components/Navbar/Navbar';
import Main from './components/Routing/Main';
import { useEffect } from 'react';
import "./assets/css/styles.css";

function App() {
	useEffect(() => {
		document.title = 'nanoCLM';
	});
	return (
		<div>
			<Navbar></Navbar>
			<Main></Main>
		</div>
	);
}

export default App;
