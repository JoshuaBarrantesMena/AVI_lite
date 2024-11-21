
const ListaAsignacion = require('../models/listaAsignacion');


exports.createListaAsignacion = async(req, res) => {
    const { id_asignacion, id_estudiante, id_documento, calificado, valor_obtenido, fecha_entregado, comentario } = req.body;

    try {
        const nuevaLista = await ListaAsignacion.create({
            id_asignacion,
            id_estudiante,
            id_documento,
            calificado,
            valor_obtenido,
            fecha_entregado,
            comentario
        });
        res.status(201).json({ message: "Documento subido con éxito", documentoId: nuevoDocumento.id_documento});
    } catch (error) {
        console.error("Error al crear la asignación:", error);
        res.status(500).json({ message: "Error al crear la asignación", error });
    }
};

exports.getAllListaAsignaciones = async(req, res) => {
    try {
        const listas = await ListaAsignacion.findAll();
        res.json(listas);
    } catch (error) {
        console.error("Error al obtener las asignaciones:", error);
        res.status(500).json({ message: "Error al obtener las asignaciones", error });
    }
};


exports.getAllListaAsignacionesEstudiantes = async (req, res) => {
    const { id_estudiante } = req.params;

    try {
        const listas = await ListaAsignacion.findAll({ where: { id_estudiante } });

        if (listas.length === 0) {
            return res.status(200).json(listas);  // Aquí se retorna para evitar enviar múltiples respuestas
        }

        // Si la lista tiene elementos, se envía esta respuesta
        return res.status(200).json(listas);

    } catch (error) {
        console.error("Error al obtener las asignaciones:", error);
        return res.status(500).json({ message: "Error al obtener las asignaciones", error });
    }
};




exports.getListaAsignacionById = async(req, res) => {

    const { id_asignacion } = req.params;
if (id_asignacion=='\n') {
        
        this.getAllListaAsignaciones(req, res);  
        return;  
    }
    try {
        const lista = await ListaAsignacion.findAll({ where: { id_asignacion } });
        if (!lista) {
            return res.status(404).json({ message: "Asignación no encontrada" });
        }
        res.json(lista);
    } catch (error) {
        console.error("Error al obtener la asignación:", error);
        res.status(500).json({ message: "Error al obtener la asignación", error });
    }
};


exports.updateListaAsignacion = async(req, res) => {
    const { id_asignacion } = req.params;
    const { id_estudiante, calificado, valor_obtenido, fecha_entregado, comentario } = req.body;

    try {
        const lista = await ListaAsignacion.findOne({ where: { id_asignacion } });
        if (!lista) {
            return res.status(404).json({ message: "Asignación no encontrada" });
        }


        lista.id_estudiante = id_estudiante;
        lista.calificado = calificado;
        lista.valor_obtenido = valor_obtenido;
        lista.fecha_entregado = fecha_entregado;
        lista.comentario = comentario;

        await lista.save();
        res.json({ message: "Asignación actualizada con éxito" });
    } catch (error) {
        console.error("Error al actualizar la asignación:", error);
        res.status(500).json({ message: "Error al actualizar la asignación", error });
    }
};


exports.deleteListaAsignacion = async(req, res) => {
    const { id_asignacion } = req.params;

    try {
        const lista = await ListaAsignacion.findOne({ where: { id_asignacion } });
        if (!lista) {
            return res.status(404).json({ message: "Asignación no encontrada" });
        }

        await lista.destroy();
        res.json({ message: "Asignación eliminada con éxito" });
    } catch (error) {
        console.error("Error al eliminar la asignación:", error);
        res.status(500).json({ message: "Error al eliminar la asignación", error });
    }
};