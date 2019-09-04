import React from 'react';
import './App.css';
import Search from './search/Search';
import SkuViewer from './sku/SkuViewer';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";


function App() {
  return (
    <div className="App">
       <Router>
          <Route path="/" exact component={Search} />
          <Route path="/sku/:id" component={SkuViewer} />
        </Router>
    </div>
  );
}

export default App;
