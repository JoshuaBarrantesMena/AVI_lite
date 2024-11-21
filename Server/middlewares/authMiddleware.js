// middlewares/authMiddleware.js
const Usuario = require('../models/Usuario');

/*const verifyToken = async (req, res, next) => {
    try {
        const token = req.headers.authorization;

       
        if (!token) {
            return res.status(403).json({ message: 'No se proporcionó un token' });
        }

        
        const usuario = await Usuario.findOne({ where: { token: token } });

       
        if (!usuario) {
            return res.status(403).json({ message: "Token inválido o usuario no encontrado" });
        }

       
    
        next();
    } catch (error) {
        console.error('Error en la verificación del token:', error);
        res.status(500).json({ message: "Error en la verificación del token" });
    }
}; */

//module.exports = verifyToken;


const authMiddleware = async (req, res, next) => {
  const token = req.headers['authorization'];

  if (!token) {
    return res.status(403).json({ message: 'Token no proporcionado' });
  }

  try {
    const usuario = await Usuario.findOne({ where: { token } });

    if (!usuario) {
      return res.status(404).json({ message: 'Usuario no encontrado' });
    }
    req.usuario = usuario;   
    next();
  } catch (error) {
    return res.status(500).json({ message: 'Error al autenticar token', error });
  }
};

module.exports = authMiddleware;
