const bcrypt = require('bcryptjs');
const crypto = require('crypto');
const Usuario = require('../models/Usuario');
const jwt = require('jsonwebtoken');
const Sequelize = require('sequelize');
const Op = Sequelize.Op;
const Universidad = require('../models/University');
exports.register = async(req, res) => {

    console.log(req.body);
    const { identificacion, pass, nombre, apellidos, correo, rol, universidad_id } = req.body;


    if (!identificacion || !pass || !nombre || !apellidos || !correo || !rol) {
        return res.status(400).json({ message: 'Faltan campos obligatorios' });
    }

    try {

        const existingUser = await Usuario.findOne({ where: { identificacion } });
        if (existingUser) {
            return res.status(400).json({ message: 'La identificación ya está registrada' });
        }




        const token = crypto.randomBytes(16).toString('hex');
        const nuevoUsuario = await Usuario.create({
            identificacion,
            pass,
            token,
            nombre,
            apellidos,
            correo,
            rol,
            universidad_id
        });

        res.status(201).json({
            message: 'Usuario registrado con éxito',
            newToken: nuevoUsuario.token
        });
    } catch (error) {
        res.status(500).json({ message: 'Error al registrar usuario', error });
    }
};


exports.login = async(req, res) => {
    console.log("Cuerpo de la solicitud:", req.body); 
    const { identificacion, pass, universidad_id } = req.body;

    
  

    try {
        const newUsuario = await Usuario.findOne({ where: { identificacion } });
        if (!newUsuario) {
            return res.status(400).json({ message: "Credenciales inválidas" });
        }

       
        if (newUsuario.pass !== pass) {
            return res.status(400).json({ message: "Credenciales inválidas" });
        }
       else if  (newUsuario.universidad_id !== universidad_id && newUsuario.rol!== 'Admin')
            {
            return res.status(400).json({ message: "Credenciales inválidas" });
        }
       

        
        res.json({ message: "Login exitoso", identificacion : newUsuario.identificacion,token: newUsuario.token, nombre: newUsuario.nombre, apellidos: newUsuario.apellidos, correo: newUsuario.correo, rol: newUsuario.rol });
    } catch (error) {
        console.error("Error en la función de login:", error);
        res.status(500).json({ message: "Error al iniciar sesión", error });
    }
};

exports.getUserById = async(req, res) => {
    const { id } = req.params;

    try {
        const newUsuario = await Usuario.findOne({ where: { identificacion: id } });
        if (!newUsuario) {
            return res.status(404).json({ message: "Usuario no encontrado" });
        }
        
        res.json(  { identificacion: newUsuario.identificacion, pass: newUsuario.pass,  token: newUsuario.token,nombre: newUsuario.nombre, apellidos:newUsuario.apellidos, correo: newUsuario.correo, rol: newUsuario.rol } );
        
    } catch (error) {
        console.error("Error al obtener el usuario:", error);
        res.status(500).json({ message: "Error al obtener el usuario", error });
    }
};
exports.getUserByToken = async(req, res) => {
    const { newToken } = req.params;

    try {
        const newUsuario = await Usuario.findOne({ where: { token: newToken } });
        if (!newUsuario) {
            return res.status(404).json({ message: "Usuario no encontrado" });
        }
        res.json(newUsuario);
    } catch (error) {
        console.error("Error al obtener el usuario:", error);
        res.status(500).json({ message: "Error al obtener el usuario", error });
    }
};

exports.getUsersByRole = async (req, res) => {
    const { rol } = req.params;

    try {
        
        const usuario = await Usuario.findAll({ where: { rol } });

       
        if (usuario.length === 0) {
            return res.status(404).json({ message: `No se encontraron usuarios con el rol ${rol}` });
        }

       
        res.json(usuario);
    } catch (error) {
        console.error("Error al obtener usuarios por rol:", error);
        res.status(500).json({ message: 'Error al obtener usuarios por rol', error });
    }
};



exports.getAllByRole = async (req, res) => {
    const { rol } = req.params;

    try {
        
        const usuario = await Usuario.findAll({ where: { rol } });

      
        if (usuario.length === 0) {
            return res.status(404).json({ message: `No se encontraron usuarios con el rol ${rol}` });
        }

       
        res.json(usuario);
    } catch (error) {
        console.error("Error al obtener usuarios por rol:", error);
        res.status(500).json({ message: 'Error al obtener usuarios por rol', error });
    }
};

exports.getAll = async (req, res) => {
   

    try {
        
        const usuario = await Usuario.findAll();

      
        if (usuario.length === 0) {
            return res.status(404).json({ message: `No se encontraron usuarios` });
        }

       
        res.json(usuario);
    } catch (error) {
        console.error("Error al obtener usuarios por rol:", error);
        res.status(500).json({ message: 'Error al obtener usuarios por rol', error });
    }
};


exports.updateUser = async(req, res) => {
    const { id } = req.params;
    const { pass, nombre, apellidos, correo, rol } = req.body;

    try {
        const usuario = await Usuario.findOne({ where: { identificacion: id} });
        if (!usuario) {
            return res.status(404).json({ message: "Usuario no encontrado" });
        }


        usuario.pass = pass;
        usuario.nombre = nombre;
        usuario.apellidos = apellidos;
        usuario.correo = correo;
        usuario.rol = rol;

        await usuario.save();
        res.json({ message: "Usuario actualizado con éxito" });
    } catch (error) {
        console.error("Error al actualizar el usuario:", error);
        res.status(500).json({ message: "Error al actualizar el usuario", error });
    }
};


exports.deleteUser = async(req, res) => {
    const { id } = req.params;

    try {
        const usuario = await Usuario.findOne({ where: { identificacion: id } });
        if (!usuario) {
            return res.status(404).json({ message: "Usuario no encontrado" });
        }

        await usuario.destroy();
        res.json({ message: "Usuario eliminado con éxito" });
    } catch (error) {
        console.error("Error al eliminar el usuario:", error);
        res.status(500).json({ message: "Error al eliminar el usuario", error });
    }
};


exports.searchStudentsByName = async (req, res) => {
    const { id, nombre } = req.params;

    try {
      
        const usuarios = await Usuario.findAll({
            where: {
                universidad_id: id,
                rol: 'Estudiante',
                
                

                nombre: {
                    [Op.like]: `%${nombre}%`  
                }
            }
        });

        if (usuarios.length === 0) {
            return res.status(404).json({ message: "No se encontraron usuarios con ese nombre en esta universidad." });
        }

        res.json(usuarios);
    } catch (error) {
        console.error("Error al buscar usuarios:", error);
        res.status(500).json({ message: "Error al buscar usuarios", error });
    }
};

