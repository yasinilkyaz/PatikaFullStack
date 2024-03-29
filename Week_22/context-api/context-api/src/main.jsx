import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import { ViewerProvider } from './contexts/viewer-context.jsx'
ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ViewerProvider>
    <App />
    </ViewerProvider>
  </React.StrictMode>,
)
