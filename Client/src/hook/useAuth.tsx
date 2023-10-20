import apiClient from "../config/axiosClient";
import { useNavigate } from "react-router-dom";
import useApp from "./useApp";
import { AxiosResponse } from "axios";
import { useToastResponses } from "./useToastResponses";

export const useAuth = () => {
  const { success, error, warning } = useToastResponses();
  const navigate = useNavigate();
  const { setUser } = useApp();
  const login = async (
    formData: any, setIsLoading: React.Dispatch<React.SetStateAction<boolean>>) => {
    try {
      const response = await apiClient.post("/auth/login", formData);
       success(
          `Bienvenido ${response.data.body.user.firstName}!!`,
          "Inicio de sesion exitoso"
        );
        localStorage.setItem("token", response.data.body.jwtToken);
        localStorage.setItem("user", JSON.stringify(response.data.body.user));
        setUser(response.data.body.user);
        if (setIsLoading) setIsLoading(false);
        navigate("/");

    } catch (errorFromCatch) {
      console.log(errorFromCatch);
      if (setIsLoading) setIsLoading(false);
      error("Valores Incorrecto.", "Por favor ingrese valores validos");
    }
  };


  const googleLogin = async (response: AxiosResponse<any>,
  setIsLoading: React.Dispatch<React.SetStateAction<boolean>>) => {
      if (response.data.ok ){
        success(
            `Bienvenido ${response.data.body.user.firstName}!!`,
            "Inicio de sesion exitoso"
        );
        localStorage.setItem("token", response.data.body.jwtToken);
        localStorage.setItem("user", JSON.stringify(response.data.body.user));
        setUser(response.data.body.user);
        if (setIsLoading) setIsLoading(false);
        navigate("/");
      }
  }

  const logout = () => {
    setUser(null);
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/");
    warning(`Sesion cerrada`, "Vuelve Pronto!");
  };

  const register = async (
    formData: any,
    setIsLoading: React.Dispatch<React.SetStateAction<boolean>>
  ) => {
    try {
      const response = await apiClient.post("/auth/register", {
        ...formData});
      const { data } = response;
      if (data.ok == true) {
        success(
          `Bienvenido ${data.body.user.firstName}`,
          "Inicio de sesion exitoso"
        );
        localStorage.setItem("token", data.body.token);
        localStorage.setItem("user", data.body.user);
        setUser(data.body.user);
        setIsLoading(false);
        navigate("/");
      }
    } catch (errorFromCatch) {
      console.log(errorFromCatch);
      setIsLoading(false);
      error("Valores Incorrecto.", "Por favor ingrese valores validos");
    }
  };

  return {
    login,
    logout,
    register,
    googleLogin
  };
};
