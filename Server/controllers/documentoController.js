// controllers/documentoController.js
const Documento = require('../models/Documento');


exports.subirDocumento = async(req, res) => {
    const { id_estudiante, url, hash_ } = req.body;

    try {
        const nuevoDocumento = await Documento.create({ id_estudiante, url, hash_ });
        res.status(201).json({ message: nuevoDocumento.id_documento  });
    } catch (error) {
        console.error("Error al subir documento:", error);
        res.status(500).json({ message: "Error al subir documento", error });
    }
};


exports.getAllDocumentos = async(req, res) => {
    try {
        const documentos = await Documento.findAll();
        res.json(documentos);
    } catch (error) {
        console.error("Error al obtener documentos:", error);
        res.status(500).json({ message: "Error al obtener documentos", error });
    }
};


exports.getDocumentoById = async(req, res) => {
    const { id } = req.params;
    if (id=='\n') {
        
        this.getAllDocumentos(req, res);  
        return;  
    }
    try {
        const documento = await Documento.findOne({ where: { id_documento: id } });
        if (!documento) {
            return res.status(404).json({ message: "Documento no encontrado" });
        }
        res.json(documento);
    } catch (error) {
        console.error("Error al obtener documento:", error);
        res.status(500).json({ message: "Error al obtener documento", error });
    }
};


exports.updateDocumento = async(req, res) => {
    const { id } = req.params;
    const { id_estudiante, url, hash_ } = req.body;

    try {
        const documento = await Documento.findOne({ where: { id_documento: id } });
        if (!documento) {
            return res.status(404).json({ message: "Documento no encontrado" });
        }

        documento.id_estudiante = id_estudiante;
        documento.url = url;
        documento.hash_ = hash_;
        await documento.save(); // Guardar cambios
        res.json({ message: "Documento actualizado con éxito" });
    } catch (error) {
        console.error("Error al actualizar documento:", error);
        res.status(500).json({ message: "Error al actualizar documento", error });
    }
};


exports.deleteDocumento = async(req, res) => {
    const { id } = req.params;

    try {
        const documento = await Documento.findOne({ where: { id_documento: id } });
        if (!documento) {
            return res.status(404).json({ message: "Documento no encontrado" });
        }

        await documento.destroy(); // Eliminar el documento
        res.json({ message: "Documento eliminado con éxito" });
    } catch (error) {
        console.error("Error al eliminar documento:", error);
        res.status(500).json({ message: "Error al eliminar documento", error });
    }
};