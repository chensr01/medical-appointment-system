import React, { createContext, useState, ReactNode } from 'react';
import axios from 'axios';
import axiosInstance from '../axiosConfig/axiosConfig'

interface AuthContextType {
  auth: { name: string; email: string; patientId: string } | null;
  login: (name: string, email: string) => void;
  logout: () => void;
}

const defaultContextValue: AuthContextType = {
  auth: null,
  login: () => {},
  logout: () => {},
};

export const AuthContext = createContext<AuthContextType>(defaultContextValue);

type AuthProviderProps = {
  children: ReactNode;
};

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [auth, setAuth] = useState<{ name: string; email: string; patientId: string } | null>(() => {
    const storedAuth = localStorage.getItem('auth');
    return storedAuth ? JSON.parse(storedAuth) : null;
  });

  // const login = (name: string, email: string) => {
  //   const authData = { name, email };
  //   setAuth(authData);
  //   localStorage.setItem('auth', JSON.stringify(authData));
  // };

  // const login = (name: string, email: string) => {
  //   axiosInstance.post('/login', {
  //     name: name,
  //     email: email
  //   })
  //   .then(response => {
  //     const authData = { name: name, email: email, patientId: response.data };
  //     setAuth(authData);
  //     localStorage.setItem('auth', JSON.stringify(authData));
  //   })
  //   .catch(error => console.error('Login failed:', error.response ? error.response.data : error));
  // };


  const login = (name: string, email: string) => {
    return axiosInstance.post('/login', {
      name: name,
      email: email
    })
    .then(response => {
      const authData = { name: name, email: email, patientId: response.data };
      setAuth(authData);
      localStorage.setItem('auth', JSON.stringify(authData));
    })
    .catch(error => {
      // Throw an error to be caught by the calling code
      throw new Error('Login failed: ' + (error.response ? error.response.data : error));
    });
  };

  const logout = () => {
    setAuth(null);
    localStorage.removeItem('auth');
  };

  return (
    <AuthContext.Provider value={{ auth, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
