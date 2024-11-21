const express = require('express');
const authMiddleware = require('../middlewares/authMiddleware'); 
const authController = require('../controllers/authController');

const router = express.Router();


router.post('/register', authController.register);


router.post('/login',authController.login);

router.get('/getAll/',authMiddleware, authController.getAll);
router.get('/:id', authMiddleware, authController.getUserById);
router.get('/token/:token', authMiddleware, authController.getUserByToken);
router.get('/role/:rol', authMiddleware,authController.getUsersByRole);
router.get('/estudiantes/:id/:nombre/',authMiddleware, authController.searchStudentsByName);


router.put('/:id', authMiddleware,authController.updateUser);


router.delete('/:id', authMiddleware,authController.deleteUser);

module.exports = router;