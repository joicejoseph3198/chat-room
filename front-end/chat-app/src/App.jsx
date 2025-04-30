import { Provider } from 'react-redux'
import './App.css'
import { AxiosProvider } from './util/axiosUtil'
import { store } from './redux/store'
import { router } from './util/router'
import { RouterProvider } from 'react-router'
import { Bounce, ToastContainer } from 'react-toastify'


function App() {
  return(
   
      <AxiosProvider>
        <Provider store={store}>
        <ToastContainer
          position="top-right"
          autoClose={5000}
          hideProgressBar={true}
          newestOnTop={true}
          closeOnClick={true}
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="dark"
          transition={Bounce}
        />
         <RouterProvider router={router}/>
        </Provider>
      </AxiosProvider>
  )
 
}

export default App
