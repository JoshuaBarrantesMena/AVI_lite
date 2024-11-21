
const IaDocumento = require('../models/IaDocumento');


exports.asociarIaDocumento = async(req, res) => {
    const { id_ia, id_documento } = req.body;

    try {
        const nuevaAsociacion = await IaDocumento.create({ id_ia, id_documento });
        res.status(201).json({ message: "IA asociada al documento con éxito", ia_documento: nuevaAsociacion });
    } catch (error) {
        console.error("Error al asociar IA con el documento:", error);
        res.status(500).json({ message: "Error al asociar IA con el documento", error });
    }
};


exports.getAllIaDocumentos = async(req, res) => {
    try {
        const iaDocumentos = await IaDocumento.findAll();
        res.json(iaDocumentos);
    } catch (error) {
        console.error("Error al obtener asociaciones IA-Documento:", error);
        res.status(500).json({ message: "Error al obtener asociaciones IA-Documento", error });
    }
};


exports.getDocumentosByIa = async(req, res) => {
    const { id_ia } = req.params;
    if (id_ia=='\n') {
        
        this.getAllIaDocumentos(req, res);  
        return;  
    }
    try {
        const documentos = await IaDocumento.findAll({ where: { id_ia } });
        if (!documentos || documentos.length === 0) {
            return res.status(404).json({ message: "No se encontraron documentos asociados a esta IA" });
        }
        res.json(documentos);
    } catch (error) {
        console.error("Error al obtener documentos por IA:", error);
        res.status(500).json({ message: "Error al obtener documentos por IA", error });
    }
};


exports.getIaByDocumento = async(req, res) => {
    const { id_documento } = req.params;

    try {
        const ia = await IaDocumento.findAll({ where: { id_documento } });
        if (!ia || ia.length === 0) {
            return res.status(404).json({ message: "No se encontró IA asociada a este documento" });
        }
        res.json(ia);
    } catch (error) {
        console.error("Error al obtener IA por documento:", error);
        res.status(500).json({ message: "Error al obtener IA por documento", error });
    }
};


exports.eliminarIaDocumento = async(req, res) => {
    const { id_ia, id_documento } = req.params;

    try {
        const iaDocumento = await IaDocumento.findOne({ where: { id_ia, id_documento } });
        if (!iaDocumento) {
            return res.status(404).json({ message: "Asociación IA-Documento no encontrada" });
        }

        await iaDocumento.destroy();
        res.json({ message: "Asociación IA-Documento eliminada con éxito" });
    } catch (error) {
        console.error("Error al eliminar asociación IA-Documento:", error);
        res.status(500).json({ message: "Error al eliminar asociación IA-Documento", error });
    }
};