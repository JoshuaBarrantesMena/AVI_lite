// controllers/cursoController.js
const Curso = require('../models/Curso');
const Facultad = require('../models/Facultad');
const Departamento = require('../models/Departamentos');
const Carrera = require('../models/Carrera');
const { Op } = require('sequelize');


exports.getCursosByUniversity= async (req, res) => {
    const { id } = req.params;

    try {
    
        const facultades = await Facultad.findAll({
            where: { universidad_id: id}
        });

        if (facultades.length === 0) {
            return res.status(404).json({ message: "No se encontraron facultades para esta universidad" });
        }

      
        const departamentos = await Departamento.findAll({
            where: {
                facultad_id: {
                    [Op.in]: facultades.map(facultad => facultad.facultad_id)
                }
            }
        });

        if (departamentos.length === 0) {
            return res.status(404).json({ message: "No se encontraron departamentos para estas facultades" });
        }

       
        const carreras = await Carrera.findAll({
            where: {
                departamento_id: {
                    [Op.in]: departamentos.map(departamento => departamento.departamento_id)
                }
            }
        });

        if (carreras.length === 0) {
            return res.status(404).json({ message: "No se encontraron carreras para estos departamentos" });
        }

     
        const cursos = await Curso.findAll({
            where: {
                carrera_id: {
                    [Op.in]: carreras.map(carrera => carrera.carrera_id)
                }
            }
        });

        if (cursos.length === 0) {
            return res.status(404).json({ message: "No se encontraron cursos para estas carreras" });
        }

 
        res.json(cursos);

    } catch (error) {
        console.error("Error al obtener cursos por universidad:", error);
        res.status(500).json({ message: "Error al obtener cursos por universidad", error });
    }
};

exports.createCurso = async(req, res) => {
    const { carrera_id, profesor_id,nombre, informacion, campos_totales } = req.body;

    try {
        const nuevoCurso = await Curso.create({ carrera_id, profesor_id, nombre, informacion, campos_totales });
        res.status(201).json({ message: "Curso creado con éxito", cursoId: nuevoCurso.curso_id });
    } catch (error) {
        console.error("Error al crear curso:", error);
        res.status(500).json({ message: "Error al crear curso", error });
    }
};

exports.getAllCursos = async(req, res) => {
    try {
        const cursos = await Curso.findAll();
        res.json(cursos);
    } catch (error) {
        console.error("Error al obtener cursos:", error);
        res.status(500).json({ message: "Error al obtener cursos", error });
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


exports.getCoursesByTeacher = async (req, res) => {
    const { id } = req.params;

    try {
        const cursos = await Curso.findAll({ where: { profesor_id:id } });
        
        if (!cursos.length) {
            return res.status(404).json({ message: "No se encontraron cursos para la carrera especificada" });
        }
        
        res.json(cursos);
    } catch (error) {
        console.error("Error al obtener cursos por carrera:", error);
        res.status(500).json({ message: "Error al obtener cursos", error });
    }
};


exports.getCoursesByCareer = async (req, res) => {
    const { id } = req.params;

    try {
        const cursos = await Curso.findAll({ where: { carrera_id:id } });
        
        if (!cursos.length) {
            return res.status(404).json({ message: "No se encontraron cursos para la carrera especificada" });
        }
        
        res.json(cursos);
    } catch (error) {
        console.error("Error al obtener cursos por carrera:", error);
        res.status(500).json({ message: "Error al obtener cursos", error });
    }
};



exports.getCursoById = async(req, res) => {
    const { id } = req.params;
    if (id=='\n') {
        
        this.getAllCursos(req, res);  
        return;  
    }
    try {
        const curso = await Curso.findOne({ where: { curso_id: id } });
        if (!curso) {
            return res.status(404).json({ message: "Curso no encontrado" });
        }
        res.json(curso);
    } catch (error) {
        console.error("Error al obtener curso:", error);
        res.status(500).json({ message: "Error al obtener curso", error });
    }
};


exports.updateCurso = async(req, res) => {
    const { id } = req.params;
    const { carrera_id,profesor_id, nombre, informacion, campos_totales } = req.body;

    try {
        const curso = await Curso.findOne({ where: { curso_id: id } });
        if (!curso) {
            return res.status(404).json({ message: "Curso no encontrado" });
        }

        curso.carrera_id = carrera_id;
        curso.nombre = nombre;
        curso.informacion = informacion;
        curso.campos_totales = campos_totales;
        await curso.save(); // Guardar cambios
        res.json({ message: "Curso actualizado con éxito" });
    } catch (error) {
        console.error("Error al actualizar curso:", error);
        res.status(500).json({ message: "Error al actualizar curso", error });
    }
};


exports.deleteCurso = async(req, res) => {
    const { id } = req.params;

    try {
        const curso = await Curso.findOne({ where: { curso_id: id } });
        if (!curso) {
            return res.status(404).json({ message: "Curso no encontrado" });
        }

        await curso.destroy(); // Eliminar el curso
        res.json({ message: "Curso eliminado con éxito" });
    } catch (error) {
        console.error("Error al eliminar curso:", error);
        res.status(500).json({ message: "Error al eliminar curso", error });
    }
};

exports.getCursosByUniversityAndName = async (req, res) => {
    const { id, nombre, profesor_id } = req.params;

    try {
        
        const facultades = await Facultad.findAll({
            where: { universidad_id: id }
        });

        if (facultades.length === 0) {
            return res.status(404).json({ message: "No se encontraron facultades para esta universidad" });
        }

       
        const departamentos = await Departamento.findAll({
            where: {
                
                facultad_id: {
                    [Op.in]: facultades.map(facultad => facultad.facultad_id)
                }
            }
        });

        if (departamentos.length === 0) {
            return res.status(404).json({ message: "No se encontraron departamentos para estas facultades" });
        }

       
        const carreras = await Carrera.findAll({
            where: {
                departamento_id: {
                    [Op.in]: departamentos.map(departamento => departamento.departamento_id)
                }
            }
        });

        if (carreras.length === 0) {
            return res.status(404).json({ message: "No se encontraron carreras para estos departamentos" });
        }

      
        const cursos = await Curso.findAll({
            where: {
                profesor_id: profesor_id,
                carrera_id: {
                    [Op.in]: carreras.map(carrera => carrera.carrera_id)
                },
                nombre: {
                    [Op.like]: `%${nombre}%` 
                }
            }
        });

        if (cursos.length === 0) {
            return res.status(404).json({ message: "No se encontraron cursos con el nombre similar" });
        }

       
        res.json(cursos);

    } catch (error) {
        console.error("Error al obtener cursos por universidad y nombre:", error);
        res.status(500).json({ message: "Error al obtener cursos por universidad y nombre", error });
    }
};
