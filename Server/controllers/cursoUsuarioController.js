// controllers/cursoUsuarioController.js
const CursoUsuario = require('../models/CursoUsuario');
const Curso= require('../models/Curso');
const Usuario = require('../models/Usuario'); // Modelo de Usuario
exports.inscribirUsuarioEnCurso = async(req, res) => {
    const { curso_id, usuario_id } = req.body;

    try {
        const nuevaInscripcion = await CursoUsuario.create({ curso_id, usuario_id });
        res.status(201).json({ message: "Usuario inscrito en el curso con éxito", inscripcion: nuevaInscripcion });
    } catch (error) {
        console.error("Error al inscribir usuario en el curso:", error);
        res.status(500).json({ message: "Error al inscribir usuario en el curso", error });
    }
};





exports.getUsuariosPorCurso = async (req, res) => {
    const { curso_id } = req.params;

    try {
        // Buscar todas las relaciones de usuarios para el curso
        const relaciones = await CursoUsuario.findAll({ where: { curso_id } });

        if (!relaciones || relaciones.length === 0) {
            return res.status(404).json({ message: "No hay usuarios inscritos en este curso" });
        }

        // Extraer los IDs de los usuarios
        const usuarioIds = relaciones.map(rel => rel.usuario_id);

        // Buscar los usuarios en la tabla Usuario
        const usuarios = await Usuario.findAll({
            where: {
                identificacion: usuarioIds,
            },
        });

        if (!usuarios || usuarios.length === 0) {
            return res.status(404).json({ message: "No se encontraron detalles de los usuarios" });
        }

        res.json(usuarios); // Devolver los detalles de los usuarios
    } catch (error) {
        console.error("Error al obtener usuarios del curso:", error);
        res.status(500).json({ message: "Error al obtener usuarios del curso", error });
    }
};



exports.getCursosPorUsuario = async(req, res) => {
    const { usuario_id } = req.params;

    try {
        const cursos = await CursoUsuario.findAll({ where: { usuario_id } });
        if (!cursos || cursos.length === 0) {
            return res.status(404).json({ message: "El usuario no está inscrito en ningún curso" });
        }
        res.json(cursos);
    } catch (error) {
        console.error("Error al obtener cursos del usuario:", error);
        res.status(500).json({ message: "Error al obtener cursos del usuario", error });
    }
};

exports.getCursosPorUsuarioConDetalles = async (req, res) => {
    const { usuario_id } = req.params;

    try {
        
        const cursosUsuario = await CursoUsuario.findAll({ where: { usuario_id } });
        if (!cursosUsuario || cursosUsuario.length === 0) {
            return res.status(404).json({ message: "El usuario no está inscrito en ningún curso" });
        }
        const cursoIds = cursosUsuario.map(curso => curso.curso_id);
        const todosLosCursos = await Curso.findAll();
        const cursosInscritos = todosLosCursos.filter(curso => cursoIds.includes(curso.curso_id));
        res.json(cursosInscritos);
    } catch (error) {
        console.error("Error al obtener cursos con detalles:", error);
        res.status(500).json({ message: "Error al obtener cursos con detalles", error });
    }
};

exports.eliminarInscripcion = async(req, res) => {
    const { curso_id, usuario_id } = req.params;

    try {
        const inscripcion = await CursoUsuario.findOne({ where: { curso_id, usuario_id } });
        if (!inscripcion) {
            return res.status(404).json({ message: "Inscripción no encontrada" });
        }

        await inscripcion.destroy();
        res.json({ message: "Usuario desinscrito del curso con éxito" });
    } catch (error) {
        console.error("Error al eliminar inscripción:", error);
        res.status(500).json({ message: "Error al eliminar inscripción", error });
    }
};