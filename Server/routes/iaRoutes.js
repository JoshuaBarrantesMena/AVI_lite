
const express = require('express');
const iaController = require('../controllers/iaController');
const authMiddleware = require('../middlewares/authMiddleware'); 

const router = express.Router();


router.post('/', authMiddleware, iaController.createRespuesta);

router.get('/', authMiddleware, iaController.getAllRespuestas);

router.get('/:id', authMiddleware, iaController.getRespuestaById);

router.put('/:id', authMiddleware, iaController.updateRespuesta);

router.delete('/:id', authMiddleware, iaController.deleteRespuesta);

module.exports = router;
